package com.atguigu.gmall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 16:32
 */
@SpringBootTest
public class RabbitMQTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg(){

    }

}
