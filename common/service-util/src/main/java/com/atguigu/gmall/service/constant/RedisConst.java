package com.atguigu.gmall.service.constant;

import java.time.Duration;

/**
 * Redis常量配置类
 * set name admin
 */
public class RedisConst {

    public static final String CATEGORY_KEY = "categorys";
    public static final String SKU_DETAIL_CACHE_KEY_PREFIX = "sku:detail:";

    public static final String LOCK_PREFIX = "lock:";
    public static final String SKU_DETAIL_LOCK_PREFIX= "lock:sku:detail:";

    public static final String SKUKEY_SUFFIX = ":info";
    //单位：秒
    public static final long SKUKEY_TIMEOUT = 24 * 60 * 60;
    // 定义变量，记录空对象的缓存过期时间
    public static final long SKUKEY_TEMPORARY_TIMEOUT = 10 * 60;
    public static final String BLOOM_SKU_ID = "skuid";
    public static final String SKU_HOTSCORE = "sku:hotscore:";

    //单位：秒 尝试获取锁的最大等待时间
//    public static final long SKULOCK_EXPIRE_PX1 = 1;
//    //单位：秒 锁的持有时间
//    public static final long SKULOCK_EXPIRE_PX2 = 1;
//    public static final String SKULOCK_SUFFIX = ":lock";
//
//    public static final String USER_KEY_PREFIX = "user:";
//    public static final String USER_CART_KEY_SUFFIX = ":cart";
//    public static final long USER_CART_EXPIRE = 60 * 60 * 24 * 7;

    //用户登录
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";
    //    public static final String userinfoKey_suffix = ":info";
    public static final int USERKEY_TIMEOUT = 60 * 60 * 24 * 7;
    public static final String USER_CART_KEY = "user:cart:";
    public static final Duration TEMP_CART_TIMEOUT = Duration.ofDays(90);
    public static final Long CART_SIZE_LIMIT = 200L;
    public static final String NO_REPEAT_TOKEN = "norepeat:token:";

    //秒杀商品前缀
/*    public static final String SECKILL_GOODS = "seckill:goods";
    public static final String SECKILL_ORDERS = "seckill:orders";
    public static final String SECKILL_ORDERS_USERS = "seckill:orders:users";
    public static final String SECKILL_STOCK_PREFIX = "seckill:stock:";
    public static final String SECKILL_USER = "seckill:user:";*/
    //用户锁定时间 单位：秒
//    public static final int SECKILL__TIMEOUT = 60 * 60 * 1;


}
