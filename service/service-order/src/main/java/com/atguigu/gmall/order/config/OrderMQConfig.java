package com.atguigu.gmall.order.config;

import com.atguigu.gmall.service.constant.MQConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 18:49
 */

@Configuration
public class OrderMQConfig {


    @Bean
    public Exchange orderEventExchange(){
        /**
         * String name: 交换机名
         * boolean durable: 持久化
         * boolean autoDelete: 自动删除
         */
        return new TopicExchange(MQConst.ORDER_EVENT_EXCHANGE, true, false);
    }

    //延迟队列
    @Bean
    public Queue orderDelayQueue(){
        /**
         * String name: 队列名
         * boolean durable, 持久化
         * boolean exclusive, 排他
         * boolean autoDelete, 是否自动删除
         * <String, Object> arguments 参数
         */
        Map<String, Object> params = new HashMap<>();
        //设置死信队列的交换机
        params.put("x-dead-letter-exchange", MQConst.ORDER_EVENT_EXCHANGE);
        //设置死信队列的路由key
        params.put("x-dead-letter-routing-key", MQConst.ORDER_TIMEOUT_BINDING_KEY);
        //设置队列消息的过期时间
        params.put("x-message-ttl", 60000 * 30);
        return new Queue(MQConst.ORDER_DELAY_QUEUE,true,false,false,params);
    }

    //死信队列
    @Bean
    public Queue orderDeadQueue(){
        return new Queue(MQConst.ORDER_DEAD_QUEUE, true,false,false);
    }

    @Bean
    public Binding orderCreateBinding(){
        /**
         * String destination, 目的地,消息到哪儿去
         * Binding.DestinationType destinationType, 目的地类型 queue/exchange/
         * String exchange, :交换机
         * String routingKey, 路由键
         * Map<String, Object> arguments 参数
         */
        return new Binding(MQConst.ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConst.ORDER_EVENT_EXCHANGE,
                MQConst.ORDER_CREATE_BINDING_KEY,null);
    }

    @Bean
    public Binding orderDeadBinding(){
        return new Binding(MQConst.ORDER_DEAD_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConst.ORDER_EVENT_EXCHANGE,
                MQConst.ORDER_TIMEOUT_BINDING_KEY,null);
    }


}
