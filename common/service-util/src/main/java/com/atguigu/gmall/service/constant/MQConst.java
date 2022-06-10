package com.atguigu.gmall.service.constant;

import org.springframework.stereotype.Component;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 18:53
 */
public class MQConst {

    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";
    public static final String ORDER_DEAD_QUEUE = "order-dead-queue";
    public static final String ORDER_CREATE_BINDING_KEY = "order.create";
    public static final String ORDER_TIMEOUT_BINDING_KEY = "order.timeout";

}
