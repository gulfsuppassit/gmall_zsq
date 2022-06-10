package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderConfirmVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86180
* @description 针对表【order_info_1(订单表 订单表)】的数据库操作Service
* @createDate 2022-06-05 17:21:25
*/
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 给前端传数据
     * @return
     */
    OrderConfirmVo getOrderConfirmVo();

    /**
     * 提交订单
     * @param tradeNo
     * @param orderSubmitVo
     */
    Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo);

    /**
     * 添加订单信息
     * @param orderSubmitVo
     * @return
     */
    public Long saveOrder(OrderSubmitVo orderSubmitVo);

    void updateStatus(ProcessStatus originStatus, ProcessStatus newStatus, Long orderId, Long userId);

    void updateOrderStatusToPAID(String outTradeNo);

    String getPaymentStatus(String outTradeNo);

    void checkOrderStatusAndUpdateStatus(String outTradeNo);
}
