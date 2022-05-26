package com.atguigu.gmall.cache.aop;

import com.atguigu.gmall.cache.constant.RedisConstant;
import com.atguigu.gmall.cache.util.CacheHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zsq
 * @Description: 缓存切面
 * @date 2022/5/25 18:53
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {

    @Autowired
    private CacheHelper cacheHelper;

    //切面直接表示环绕通知,value值为切入点是什么,@annotation指带指定注解的为切入点
    @Around(value = "@annotation(com.atguigu.gmall.cache.anno.Cache)")
    public Object around(ProceedingJoinPoint joinPoint){
        //环绕切面的常见三步走
        //获取切入点的参数
        Object[] args = joinPoint.getArgs();
        //先查缓存
        Object data = null;
        try {
            //1.前置通知
            log.info("查缓存");
//            String cacheKey = RedisConst.SKU_DETAIL_CACHE_KEY_PREFIX+args[0];
            String cacheKey = cacheHelper.getCacheKey(joinPoint);
            data = cacheHelper.getData(cacheKey,joinPoint);
            if (data == null) {
                log.info("缓存未命中,回源查数据库");
                //查看是否使用布隆,如果有bloomName则代表使用布隆,无bloomName则是不使用布隆
                String  bloomName = cacheHelper.getBloomName(joinPoint);
                if (StringUtils.isEmpty(bloomName)) {
                    log.info("bloomName为空,则代表不使用布隆,直接查库");
                    String lockKey = RedisConstant.LOCK_PREFIX+cacheKey;
                    boolean lock = cacheHelper.tryLock(lockKey);
                    if (lock){
                        log.info("尝试抢锁成功");
                        data = joinPoint.proceed(args);
                        cacheHelper.saveData(cacheKey,data);
                        cacheHelper.unlock(lockKey);
                        return data;
                    }else{
                        log.info("尝试抢锁失败,睡一秒,查缓存");
                        Thread.sleep(1000);
                        data = cacheHelper.getData(cacheKey,joinPoint);
                        return data;
                    }
                }else{
                    log.info("bloomName不为空,则代表使用布隆,先问布隆再查库查库");
                    boolean isContain = cacheHelper.isContainsInBloom(bloomName,joinPoint);
                    if (isContain){
                        log.info("布隆说有,上锁查库");
                        String lockKey = RedisConstant.LOCK_PREFIX+cacheKey;
                        boolean lock = cacheHelper.tryLock(lockKey);
                        if (lock){
                            log.info("尝试抢锁成功");
                            data = joinPoint.proceed(args);
                            //查到数据后,存入缓存中
                            cacheHelper.saveData(cacheKey,data);
                            cacheHelper.unlock(lockKey);
                            return data;
                        }else{
                            log.info("尝试抢锁失败,睡一秒,查缓存");
                            Thread.sleep(1000);
                            data = cacheHelper.getData(cacheKey,joinPoint);
                            return data;
                        }
                    }
                    log.info("布隆说没有");
                    return null;
                }

            }
            //2.正常通知
            log.info("缓存中有");
            return data;
        } catch (Throwable e) {
            e.printStackTrace();
            //3.异常通知
        }finally {
            //后置通知
        }
        return data;
    }

}
