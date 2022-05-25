package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.DetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailTo;
import com.atguigu.gmall.service.constant.RedisConst;
import com.atguigu.gmall.service.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 16:47
 */
@Slf4j
@Service
public class DetailServiceImpl implements DetailService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Qualifier("corePool")
    @Autowired
    private ThreadPoolExecutor corePool;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private RBloomFilter<Object> skuIdBloom;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 使用reidsson的分布式锁
     * @param skuId:skuInfo的主键
     * @return :详情to
     */
    /*@Override
    public ItemDetailTo getDetail(Long skuId) {

        return null;
    }*/

    /**
     * 使用redis原生的分布式锁
     * @param skuId
     * @return
     */
    public ItemDetailTo getDetail(Long skuId){
        ItemDetailTo data = cacheService.getData(RedisConst.SKU_DETAIL_CACHE_KEY_PREFIX + skuId, new TypeReference<ItemDetailTo>() {
        });
        if (data == null){
            log.info("缓存中没有,准备回源..");
            //回源前问下布隆,这个东西有没有
            boolean isContains = skuIdBloom.contains(skuId);
            if (isContains){
                ItemDetailTo detailFromDB = null;
                //加锁解决缓存击穿的问题
                String token = UUID.randomUUID().toString();
                //setIfAbsent相当于setnx,使用redis的setnx(只在键不存在的时候才可以设置键的值)的特性,在redis中添加lock锁
                Boolean lock = redisTemplate.opsForValue().setIfAbsent(RedisConst.LOCK_PREFIX+skuId, token,10, TimeUnit.SECONDS);
                if (lock){
                    log.info("分布式加锁成功：SkuDetail：{} 真的查库：", skuId);
                    try{

                        log.info("布隆中数据库中有detailId为{}的数据,正在回源查数据...",skuId);
                        detailFromDB = getDetailFromDB(skuId);
                        cacheService.save(RedisConst.SKU_DETAIL_CACHE_KEY_PREFIX + skuId, detailFromDB);
                    }finally {
                        //抢到锁,就要释放锁
//                        redisTemplate.opsForValue().get(RedisConst.LOCK_PREFIX+skuId)
                        //将判断是否为自己的锁和删锁二合一,变为原子性的操作
                        String deleteScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        // List<K> var2, Object... var3
                        //redis.execute(RedisScript<>()):redis执行lua语言脚本
                        Long result = redisTemplate.execute(new DefaultRedisScript<>(deleteScript,Long.class),
                                Arrays.asList(RedisConst.LOCK_PREFIX+skuId),
                                token
                                );
                        if(result == 1){
                            log.info("我的分布式锁解锁完成.");
                        }else {
                            //别人的锁【说明之前由于业务卡顿，续期失败等原因，锁被自动释放，被别人抢到】
                            log.info("这是别人的锁，我不能删.");
                        }
                    }
                }else{
                    log.info("分布式加锁失败：SkuDetail：{}",skuId);
                    //抢锁失败后,不要再走循环强锁了,睡1秒,等待抢到锁的人查到数据后,存入缓存,直接从缓存里拿
                    try {
                        Thread.sleep(1000);
                        detailFromDB = cacheService.getData(RedisConst.LOCK_PREFIX + skuId, new TypeReference<ItemDetailTo>() {
                        });
                    } catch (InterruptedException e) {

                    }
                }
                return detailFromDB;
            }
            log.info("布隆中显示数据库中无此数据,bloom防火墙拦截打回：detail:{}",skuId);
            return null;
        }
        log.info("SkuDetail:{}缓存命中:",skuId);
        return data;
    }

    @Override
    public ItemDetailTo getDetailFromDB(Long skuId){
        ItemDetailTo itemDetailTo = new ItemDetailTo();
        CompletableFuture<Void> categoryTask = CompletableFuture.runAsync(() -> {
            Result<BaseCategoryView> baseCategoryViewResult = productFeignClient.getCategoryView(skuId);
            if (baseCategoryViewResult.isOk()) {
                itemDetailTo.setCategoryView(baseCategoryViewResult.getData());
            }
        }, corePool);

        CompletableFuture<Void> skuInfoTask =  CompletableFuture.runAsync(()->{
            Result<SkuInfo> skuInfoResult = productFeignClient.getSkuInfo(skuId);
            if (skuInfoResult.isOk()){
                itemDetailTo.setSkuInfo(skuInfoResult.getData());
            }
        },corePool);

        CompletableFuture<Void> priceTask = CompletableFuture.runAsync(()->{
            Result<BigDecimal> priceResult = productFeignClient.getPrice(skuId);
            if (priceResult.isOk()){
                itemDetailTo.setPrice(priceResult.getData());
            }
        },corePool);

        CompletableFuture<Void> spuSaleAttrTask = CompletableFuture.runAsync(()->{
            Result<List<SpuSaleAttr>> listResult = productFeignClient.getSpuSaleAttrAndValueBySkuId(skuId);
            if (listResult.isOk()){
                itemDetailTo.setSpuSaleAttrList(listResult.getData());
            }
        },corePool);

        CompletableFuture<Void> valueJsonTask = CompletableFuture.runAsync(()->{
            Result<Map<String, String>> valuesSkuJsonResult = productFeignClient.getValuesSkuJson(skuId);
            if (valuesSkuJsonResult.isOk()) {
                Map<String, String> data = valuesSkuJsonResult.getData();
                itemDetailTo.setValuesSkuJson(JSONs.toStr(data));
            }
        },corePool);

        CompletableFuture.allOf(categoryTask,skuInfoTask,priceTask,spuSaleAttrTask,valueJsonTask).join();

        return itemDetailTo;
    }



}
