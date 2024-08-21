package com.itheima.publisher;


import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(classes = PublisherApplication.class)
@Slf4j
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp qqqqqq!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message);
    }

    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message);
    }

    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    void testPublisherConfirm() {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                // log.error("send message fail", ex);
                System.out.println("send message fail");
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if (result.isAck()) { // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    // log.debug("发送消息成功，收到 ack!");
                    System.out.println("发送消息成功，收到 ack!");
                } else { // result.getReason()，String类型，返回nack时的异常描述
                    // log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                    System.out.println("发送消息失败，收到 nack, reason : " + result.getReason());
                }
            }
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("hmall.direct", "y11ellow", "hello", cd);
    }
}
