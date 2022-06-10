package com.atguigu.gmall.order.listener.mq;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.to.OrderCreateTo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.service.constant.MQConst;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 19:31
 */
@Slf4j
@Component
public class OrderCloseListener {

    @Autowired
    OrderInfoService orderInfoService;
    //消费死信队列
    @RabbitListener(queues = MQConst.ORDER_DEAD_QUEUE)
    public void closeOrder(Message message, Channel channel) {
        OrderCreateTo orderCreateTo = null;
        try {
            orderCreateTo = JSONs.strToObj(new String(message.getBody()), new TypeReference<OrderCreateTo>() {
            });
            Long orderId = orderCreateTo.getOrderId();
            Long userId = orderCreateTo.getUserId();
            //原子关闭订单
            orderInfoService.updateStatus(ProcessStatus.UNPAID, ProcessStatus.CLOSED, orderId, userId);
            log.info("订单:{}已处理",orderCreateTo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单[{}]的处理出现错误:{}",orderCreateTo, e);
        }
        //deliveryTag , multiple
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("mq发生异常：错误信息：{}，订单关单消息可能处理失败：消息内容：{}",e,orderCreateTo);
        }
    }

}
