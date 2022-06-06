package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.UserAuthTO;
import com.atguigu.gmall.service.constant.RedisConst;
import com.fasterxml.jackson.core.type.TypeReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 18:15
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignClient productFeignClient;

    @Qualifier("corePool")
    @Autowired
    ThreadPoolExecutor corePool;

    /**
     * 判断购物车是否溢出,true代表溢出了,false代表没溢出
     * @param cartKey
     * @return
     */
    @Override
    public void validationCartOverFlow(String cartKey) {
        Long size = redisTemplate.boundHashOps(cartKey).size();
        if (size >= RedisConst.CART_SIZE_LIMIT) {
            throw new GmallException(ResultCodeEnum.CART_OVERFLOW);
        }
    }

    @Override
    public void setCartTimeOut(String cartKey) {
        //设置临时购物车的过期时间
        redisTemplate.expire(cartKey, RedisConst.TEMP_CART_TIMEOUT);
    }

    @Override
    public CartInfo addCart(Long skuId, Integer skuNum) {
        String cartKey = determinCartKey();
        CartInfo cartInfo = saveSkuToCart(skuId, skuNum, cartKey);

        validationCartOverFlow(cartKey);


        if (AuthUtil.getUserAuth().getUserId() == null){
            //说明用户没登录,设置临时购物车的过期时间
            setCartTimeOut(cartKey);
        }

        return cartInfo;
    }

    @Override
    public List<CartInfo> getCartList() {
        BoundHashOperations<String, String, String> userCart = getUserCart();
        BoundHashOperations<String, String, String> tempCart = getTempCart();

        //1.判断是否需要合并购物车(有userId和userTempId)
        if (userCart != null && tempCart != null && tempCart.size() > 0){
            //直接判断,如果两个购物车相加数量溢出,则直接跑出溢出异常
            if ((userCart.size()+ tempCart.size()) >= RedisConst.CART_SIZE_LIMIT){
                throw new GmallException(ResultCodeEnum.CART_MERGE_OVERFLOW);
            }
            //3.如果需要合并 [登录了,用户购物车有商品,临时购物车也有商品]
            //4.合并操作? 把临时购物车的商品一道用户登录的购物车中,然后删除临时购物车的数据
            String userKey = getUserKey();
            String tempKey = getTempKey();
            List<CartInfo> tempCartInfos = getInfos(tempKey);
            //遍历临时数据,将临时数据添加到用户购物车中
            tempCartInfos.stream().forEach(cartInfo -> saveSkuToCart(cartInfo.getSkuId(), cartInfo.getSkuNum(), userKey));
            //删除临时购物车
            deleteCart(tempKey);
            //5.返回合并后的所有数据
            List<CartInfo> infos = getInfos(userKey);

            updatePriceBatch(userKey);
            return infos;
        }else{
            //2.如果不需要合并[没登录 只有UserTempId][登录了,但是临时鼓舞车没东西][登录了,里面是购物车被合并过了]
            //获取键
            String cartKey = determinCartKey();
            //获取所有该用户的所有商品
            List<CartInfo> cartInfos = getInfos(cartKey);

            updatePriceBatch(cartKey);

            return cartInfos;
        }

    }

    /**
     * 批量更新价格
     * @param cartKey
     */
    private void updatePriceBatch(String cartKey) {
        List<CartInfo> infos = getInfos(cartKey);
        corePool.submit(() -> infos.stream().forEach(info -> {
            BigDecimal price = productFeignClient.getPrice(info.getSkuId()).getData();
            updateCartInfoPrice(cartKey, info.getSkuId(), price);
            info.setSkuPrice(price);
        }),corePool);
    }

    public void updateCartInfoPrice(String cartKey, Long skuId, BigDecimal price) {
        CartInfo cart = getCart(skuId, cartKey);
        cart.setSkuPrice(price);
        addCartInfo(skuId, cart, cartKey);
    }

    private List<CartInfo> getInfos(String cartKey) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        List<String> values = ops.values();
        List<CartInfo> cartInfos = values.stream().map((cartInfoStr) -> {
            CartInfo cartInfo = JSONs.strToObj(cartInfoStr, new TypeReference<CartInfo>() {
            });
            return cartInfo;
        }).sorted((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()))
          .collect(Collectors.toList());
        return cartInfos;
    }

    @Override
    public void addToCart(Long skuId, Integer num) {
        String cartKey = determinCartKey();
        CartInfo cartInfo = getCart(skuId, cartKey);
        cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
        cartInfo.setUpdateTime(new Date());
        addCartInfo(skuId,cartInfo, cartKey);
    }

    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        String cartKey = determinCartKey();
        CartInfo cartInfo = getCart(skuId, cartKey);
        cartInfo.setIsChecked(isChecked);
        addCartInfo(skuId, cartInfo, cartKey);
    }

    @Override
    public void deleteCart(Long skuId) {
        String cartKey = determinCartKey();
        deleteCartBySkuId(skuId, cartKey);
    }

    @Override
    public void deleteChecked() {
        String cartKey = determinCartKey();
        List<CartInfo> cartList = getInfos(cartKey);
        Object[] objects = cartList.stream().filter(item -> item.getIsChecked() == 1)
                .map(item -> item.getSkuId().toString())
                .toArray();
        if (objects != null && objects.length > 0) deleteCartBySkuId(cartKey, objects);
    }


    @Override
    public void deleteCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    private String getUserKey(){
        UserAuthTO userAuth = AuthUtil.getUserAuth();
        Long userId = userAuth.getUserId();
        if (userId != null){
            String cartKey = RedisConst.USER_CART_KEY + userId;
            return cartKey;
        }
        return null;
    }

    private String getTempKey(){
        UserAuthTO userAuth = AuthUtil.getUserAuth();
        String userTempId = userAuth.getUserTempId();
        if (!StringUtils.isEmpty(userTempId)){
            String cartKey = RedisConst.USER_CART_KEY + userTempId;
            return cartKey;
        }
        return null;
    }
    private BoundHashOperations<String, String, String> getUserCart(){
        UserAuthTO userAuth = AuthUtil.getUserAuth();
        Long userId = userAuth.getUserId();
        if (userId != null){
            String cartKey = RedisConst.USER_CART_KEY + userId;
            return redisTemplate.boundHashOps(cartKey);
        }
        return null;
    }

    private BoundHashOperations<String, String, String> getTempCart(){
        UserAuthTO userAuth = AuthUtil.getUserAuth();
        String userTempId = userAuth.getUserTempId();
        if (!StringUtils.isEmpty(userTempId)){
            String cartKey = RedisConst.USER_CART_KEY + userTempId;
            return redisTemplate.boundHashOps(cartKey);
        }
        return null;
    }

    private void deleteCartBySkuId(String cartKey, Object... skuId) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        ops.delete(skuId);
    }
    private void deleteCartBySkuId(Long skuId, String cartKey) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        ops.delete(skuId.toString());
    }

    private void addCartInfo(Long skuId, CartInfo cartInfo, String cartKey) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        ops.put(skuId.toString(), JSONs.toStr(cartInfo));
    }

    private CartInfo getCart(Long skuId, String cartKey) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        String json = ops.get(skuId.toString());
        return JSONs.strToObj(json, new TypeReference<CartInfo>() {
        });
    }

    private CartInfo saveSkuToCart(Long skuId, Integer skuNum, String cartKey) {

        BoundHashOperations<String, String, String> cart = redisTemplate.boundHashOps(cartKey);
        Boolean hasKey = cart.hasKey(skuId.toString());
        if (Boolean.TRUE.equals(hasKey)){
            //添加过该商品,则原商品数量加skuNum
            String json = cart.get(skuId.toString());
            CartInfo cartInfo = JSONs.strToObj(json, new TypeReference<CartInfo>() {
            });
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            cart.put(skuId.toString(), JSONs.toStr(cartInfo));
            return cartInfo;
        }else{
            //没添加过该商品,则保存该商品数据
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId).getData();
            CartInfo cartInfo = convertSkuInfoToCartItem(skuInfo);
            cartInfo.setSkuNum(skuNum);

            cart.put(skuId.toString(), JSONs.toStr(cartInfo));
            return cartInfo;
        }
    }

    private CartInfo convertSkuInfoToCartItem(SkuInfo skuInfo) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setId(skuInfo.getId());
        if (AuthUtil.getUserAuth().getUserId() == null){
            cartInfo.setUserId(AuthUtil.getUserAuth().getUserTempId());
        }else{
            cartInfo.setUserId(AuthUtil.getUserAuth().getUserId().toString());
        }
        cartInfo.setSkuId(skuInfo.getId());
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setIsChecked(1);
        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setSkuPrice(skuInfo.getPrice());
        return cartInfo;
    }

    private String determinCartKey() {
        UserAuthTO userAuth = AuthUtil.getUserAuth();
        Long userId = userAuth.getUserId();
        String userTempId = userAuth.getUserTempId();
        if (userId == null) {
            //用户未登录
            return RedisConst.USER_CART_KEY + userTempId;
        } else {
            //用户登录了
            return RedisConst.USER_CART_KEY + userId;
        }
    }
}
