package com.atguigu.gmall.order.service.impl;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.pay.PayFeignClient;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.PaymentWay;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.to.OrderCreateTo;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.service.constant.MQConst;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;

import java.sql.Wrapper;
import java.util.Date;
import com.atguigu.gmall.model.activity.CouponInfo;
import java.math.BigDecimal;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.cart.CartInfoForOrderVo;
import com.atguigu.gmall.model.order.OrderSubmitVo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.service.constant.RedisConst;

import com.atguigu.gmall.model.order.OrderConfirmVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 86180
* @description 针对表【order_info_1(订单表 订单表)】的数据库操作Service实现
* @createDate 2022-06-05 17:21:25
*/
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private PayFeignClient payFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private WareFeignClient wareFeignClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OrderDetailService orderDetailService;
    @Qualifier("corePool")
    @Autowired
    private ThreadPoolExecutor corePool;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public OrderConfirmVo getOrderConfirmVo() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        Long userId = AuthUtil.getUserAuth().getUserId();
        System.out.println(userId);
        Result<List<CartInfo>> checkedItemsResult = cartFeignClient.getCheckedItems();
        if (checkedItemsResult.isOk()) {

            //设置前端需要的购物车项vo
            List<CartInfo> cartInfos = checkedItemsResult.getData();
            List<CartInfoForOrderVo> vos = packageCartInfoForOrderVo(cartInfos);
            orderConfirmVo.setDetailArrayList(vos);

            //设置总量
            Integer totalNum = getTotalNum(cartInfos);
            orderConfirmVo.setTotalNum(totalNum);

            //设置总价
            BigDecimal totalAmount = getTotalAmount(vos);
            orderConfirmVo.setTotalAmount(totalAmount);

            //设置用户地址列表
            List<UserAddress> userAddressList = userFeignClient.getUserAddressList().getData();
            orderConfirmVo.setUserAddressList(userAddressList);
        }

        //设置 tradeNo； 防重令牌，给redis一个
        String tradeNo = generateTradeNo();
        log.info("令牌"+tradeNo);
        //防重令牌，给页面一个
        orderConfirmVo.setTradeNo(tradeNo);

        return orderConfirmVo;
    }

    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo) {
        //1.验令牌
        boolean checkTradeNo = checkTradeNo(tradeNo);
        if (!checkTradeNo){
            //令牌错误
            throw new GmallException(ResultCodeEnum.REQ_ILLEGAL_TOKEN_ERROR);
        }
        //2.验价格
        //2.1获取前端传来的总价
        BigDecimal frontTotal = orderSubmitVo.getOrderDetailList().stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())))
                .reduce((a, b) -> a.add(b))
                .get();
        //2.2获取真正的总价
        Result<List<CartInfo>> checkedItemResult = cartFeignClient.getCheckedItems();
        BigDecimal backTotal = null;
        if (checkedItemResult.isOk()) {
            backTotal = checkedItemResult.getData().stream()
                    .map((item)->{
                        BigDecimal price = productFeignClient.getPrice(item.getSkuId()).getData();
                        return price.multiply(new BigDecimal(item.getSkuNum().toString()));
                    })
                    .reduce((a, b) -> a.add(b)).get();
        }

        //2.3比较值
        if (frontTotal.compareTo(backTotal) != 0){
            throw new GmallException(ResultCodeEnum.PRICE_NOTEQUAL);
        }

        //3.验库存
        List<String> noStock = new ArrayList<>();
        checkedItemResult.getData().stream()
                .forEach(item->{
                    String hasStock = wareFeignClient.hasStock(item.getSkuId(), item.getSkuNum());
                    if (!"1".equals(hasStock)){
                        noStock.add("【"+item.getSkuName()+"】没有库存了");
                    }
                });
        if (noStock.size() > 0){
            String msg = noStock.stream().reduce((a, b) -> a + b).get();
            throw new GmallException(msg, ResultCodeEnum.NO_STOCK.getCode());
        }

        //4.保存订单
        Long orderId = saveOrder(orderSubmitVo);
        RequestAttributes oldRequest = RequestContextHolder.getRequestAttributes();
        //5.删除购物车中选中订单
        corePool.submit(()->{
            RequestContextHolder.setRequestAttributes(oldRequest);
            cartFeignClient.deleteChecked();
        });

        //6.返回订单id
        return orderId;
    }
    @Transactional
    @Override
    public Long saveOrder(OrderSubmitVo orderSubmitVo) {
        OrderInfo orderInfo = prepareOrderInfo(orderSubmitVo);

        //1、将vo带来的数据，转成订单保存数据库中的数据模型
        save(orderInfo);
        //2、保存 order_detail
        List<OrderDetail> orderDetails = prepareOrderDetail(orderInfo);
        orderDetailService.saveBatch(orderDetails);

        //7.发消息,延迟队列+死信队列,修改30分钟后为支付的订单
        OrderCreateTo orderCreateTo = new OrderCreateTo(AuthUtil.getUserAuth().getUserId(), orderInfo.getId());
        sendOrderCreateMag(orderCreateTo);

        return orderInfo.getId();
    }

    @Override
    public void updateStatus(ProcessStatus originStatus, ProcessStatus newStatus, Long orderId, Long userId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(newStatus.name());
        orderInfo.setProcessStatus(newStatus.name());
        orderInfo.setId(orderId);
        orderInfo.setUserId(userId);
        baseMapper.updateStatus(orderInfo,originStatus.name());
    }

    @Override
    public void updateOrderStatusToPAID(String outTradeNo) {
//        Long userId = AuthUtil.getUserAuth().getUserId();
        String[] split = outTradeNo.split("-");
        String userId = split[2];
        baseMapper.updateOrderStatusToPAID(ProcessStatus.PAID.name(),ProcessStatus.PAID.getOrderStatus().name(),outTradeNo,Long.parseLong(userId));
    }

    @Override
    public String getPaymentStatus(String outTradeNo) {
        String[] split = outTradeNo.split("-");
        String userId = split[2];
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.lambdaQuery(OrderInfo.class).select(OrderInfo::getProcessStatus)
                .eq(OrderInfo::getOutTradeNo, outTradeNo)
                .eq(OrderInfo::getUserId, Long.parseLong(userId));
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        return orderInfo.getProcessStatus();
    }

    @Override
    public void checkOrderStatusAndUpdateStatus(String outTradeNo) {
        String aliPayStatus = payFeignClient.queryTrade(outTradeNo).getData();
        String paymentStatus = getPaymentStatus(outTradeNo);
        if("TRADE_SUCCESS".equals(aliPayStatus) && (paymentStatus.equals(ProcessStatus.UNPAID.name()) || paymentStatus.equals(ProcessStatus.CLOSED.name()))){
            //改成已支付即可
            updateOrderStatusToPAID(outTradeNo);
        }
    }


    private void sendOrderCreateMag(OrderCreateTo orderCreateTo) {
        String json = JSONs.toStr(orderCreateTo);
        rabbitTemplate.convertAndSend(MQConst.ORDER_EVENT_EXCHANGE, MQConst.ORDER_CREATE_BINDING_KEY, json);
    }

    private List<OrderDetail> prepareOrderDetail(OrderInfo orderInfo) {
        List<OrderDetail> orderDetails = cartFeignClient.getCheckedItems().getData().stream()
                .map(item -> {
                    OrderDetail orderDetail = new OrderDetail();

                    Long userId = AuthUtil.getUserAuth().getUserId();
                    orderDetail.setUserId(userId);

                    orderDetail.setOrderId(orderInfo.getId());
                    orderDetail.setSkuId(item.getSkuId());
                    orderDetail.setSkuName(item.getSkuName());
                    orderDetail.setImgUrl(item.getImgUrl());
                    orderDetail.setOrderPrice(item.getSkuPrice());
                    orderDetail.setSkuNum(item.getSkuNum());
                    orderDetail.setHasStock(wareFeignClient.hasStock(item.getSkuId(), item.getSkuNum()));
                    orderDetail.setCreateTime(new Date());
                    orderDetail.setSplitTotalAmount(new BigDecimal("0"));
                    orderDetail.setSplitActivityAmount(new BigDecimal("0"));
                    orderDetail.setSplitCouponAmount(new BigDecimal("0"));
                    return orderDetail;
                }).collect(Collectors.toList());

        return orderDetails;
    }

    private OrderInfo prepareOrderInfo(OrderSubmitVo orderSubmitVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setConsignee(orderSubmitVo.getConsignee());
        orderInfo.setConsigneeTel(orderSubmitVo.getConsigneeTel());
        BigDecimal totoalAmount = orderSubmitVo.getOrderDetailList().stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum().toString())))
                .reduce((a, b) -> a.add(b)).get();
        orderInfo.setTotalAmount(totoalAmount);

        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        orderInfo.setUserId(AuthUtil.getUserAuth().getUserId());
        orderInfo.setPaymentWay(PaymentWay.ONLINE.getComment());
        orderInfo.setDeliveryAddress(orderSubmitVo.getDeliveryAddress());
        orderInfo.setOrderComment(orderSubmitVo.getOrderComment());
        long timeStamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        //对外交易号
        orderInfo.setOutTradeNo("GAMLL-" + timeStamp + "-"+ AuthUtil.getUserAuth().getUserId() + "-" + uuid);
        //交易体: 所有购买的商品名
        String tradeBody = orderSubmitVo.getOrderDetailList().stream()
                .map(CartInfoForOrderVo::getSkuName)
                .reduce((a, b) -> a + ";" + b).get();
        orderInfo.setTradeBody(tradeBody);
        orderInfo.setCreateTime(new Date());
        orderInfo.setExpireTime(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        //物流追踪号
        orderInfo.setTrackingNo("");
        //拆单：父子订单
        orderInfo.setParentOrderId(0L);

        orderInfo.setImgUrl(orderSubmitVo.getOrderDetailList().get(0).getImgUrl());
//        orderInfo.setOrderDetailList(orderSubmitVo.getOrderDetailList());

        orderInfo.setWareId("");
        orderInfo.setProvinceId(0L);

        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
        //可退款日期 7天
        orderInfo.setRefundableTime(null);
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());

        return orderInfo;
    }

    public String generateTradeNo() {

        //1、生成防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //2、保存到redis； 每个数据都应该有过期时间
        redisTemplate.opsForValue().set(RedisConst.NO_REPEAT_TOKEN + token, "1", 10, TimeUnit.MINUTES);
        return token;
    }

    public boolean checkTradeNo(String token) {
        //1、原子验令牌+删令牌
        String script = "if redis.call('get', KEYS[1]) == '1' then return redis.call('del', KEYS[1]) else return 0 end";
        //2、执行
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(RedisConst.NO_REPEAT_TOKEN + token), "1");
        return result == 1L;
    }

    private BigDecimal getTotalAmount(List<CartInfoForOrderVo> cartInfos) {
        return cartInfos.stream()
                .map(cartInfo -> cartInfo.getOrderPrice().multiply(new BigDecimal(cartInfo.getSkuNum().toString())))
                .reduce(BigDecimal::add).get();
    }

    private Integer getTotalNum(List<CartInfo> cartInfos) {
        return cartInfos.stream().map(CartInfo::getSkuNum).reduce(Integer::sum).get();
    }

    private List<CartInfoForOrderVo> packageCartInfoForOrderVo(List<CartInfo> cartInfos) {
        List<CartInfoForOrderVo> vos = cartInfos.stream().map(item -> {
            CartInfoForOrderVo orderVo = new CartInfoForOrderVo();
            orderVo.setImgUrl(item.getImgUrl());
            orderVo.setSkuName(item.getSkuName());
            BigDecimal price = productFeignClient.getPrice(item.getSkuId()).getData();
            orderVo.setOrderPrice(price);
            orderVo.setSkuNum(item.getSkuNum());
            String stock = wareFeignClient.hasStock(item.getSkuId(), item.getSkuNum());
            orderVo.setStock(stock);
            return orderVo;
        }).collect(Collectors.toList());
        return vos;
    }
}




