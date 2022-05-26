package com.atguigu.gmall.cache.util;

import com.atguigu.gmall.cache.anno.Cache;
import com.atguigu.gmall.cache.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zsq
 * @Description: 缓存的帮助类
 * @date 2022/5/25 19:47
 */
@Component
public class CacheHelper {

    //获取表达式解析器
    private static final SpelExpressionParser parser = new SpelExpressionParser();


    @Autowired
    private CacheService cacheService;
    @Autowired
    private Map<String,RBloomFilter<Object>> blooms;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取注解中传入的缓存key
     * @param joinPoint
     * @return
     */
    public String getCacheKey(ProceedingJoinPoint joinPoint) {
        Cache cache = getMethodAnnotation(joinPoint);
        String cacheKey = cache.cacheKey();
        String value = parseExpression(joinPoint, cacheKey, String.class);
        return value;
    }

    /**
     * 获取切入点方法上的Cache注解
     * @param joinPoint
     * @return
     */
    private Cache getMethodAnnotation(ProceedingJoinPoint joinPoint) {
        //获取方法完整签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //通过完整签名获取方法本身
        Method method = signature.getMethod();
        //通过方法获取方法上的注解
        Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);
        return cache;
    }

    /**
     * 解析表达式
     * @param joinPoint:         切入点
     * @param expressionContent: 表达式
     * @param clz:               表达式解析后的类型,不想精确指定,项自动确定的传Object.class
     * @param <T>:               泛型
     * @return : 返回传入的类型
     */
    public <T> T parseExpression(ProceedingJoinPoint joinPoint, String expressionContent, Class<T> clz) {
        //计算表达式
        Expression expression = parser.parseExpression(expressionContent, ParserContext.TEMPLATE_EXPRESSION);
        //获取上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //放入想要动态使用的参数
        Object[] args = joinPoint.getArgs();
        context.setVariable("args", args);
        //未来想要用什么,就在这里放就行
        //返回表达式解析后的数据
        return expression.getValue(context, clz);
    }

    /**
     * 获取缓存中的数据
     * @param cacheKey: 缓存key
     * @param joinPoint: 目标方法
     * @return : 缓存中想要的数据
     */
    public Object getData(String cacheKey, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //通过签名获取目标方法,通过目标方法获取精确的返回值类型
        Type type = method.getGenericReturnType();
        return cacheService.getData(cacheKey, new TypeReference<Object>() {
            @Override
            public Type getType() {
                //在这里指定精确地返回类型
                return type;
            }
        });
    }

    /**
     * 获取想要布隆查找的值
     * @return
     */
    public Object getBloomValue(ProceedingJoinPoint joinPoint){
        Cache cache = getMethodAnnotation(joinPoint);
        String bloomValue = cache.bloomValue();
        return parseExpression(joinPoint, bloomValue, Object.class);
    }

    /**
     * 询问布隆是否存在这个值
     * @param bloomName:布隆名称
     * @param joinPoint:切入点
     * @return : 返回布隆是否存在这个值的结果
     */
    public boolean isContainsInBloom(String bloomName, ProceedingJoinPoint joinPoint) {
        RBloomFilter<Object> bloomFilter = blooms.get(bloomName);
        Object bloomValue = getBloomValue(joinPoint);
        return bloomFilter.contains(bloomValue);
    }

    /**
     * 尝试加锁
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    /**
     * 解锁
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.isLocked())lock.unlock();
        }catch (Exception ignored){}
    }

    /**
     * 获取布隆名称
     * @param joinPoint
     * @return
     */
    public String getBloomName(ProceedingJoinPoint joinPoint) {
        Cache cache = getMethodAnnotation(joinPoint);
        assert cache != null;
        return cache.bloomName();
    }

    /**
     * 把从数据库查出的数据存入缓存中
     *
     * @param cacheKey
     * @param data
     */
    public void saveData(String cacheKey, Object data) {
        cacheService.save(cacheKey,data);
    }
}
