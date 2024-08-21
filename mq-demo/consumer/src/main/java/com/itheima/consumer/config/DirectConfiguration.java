package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class DirectConfiguration {

    @Bean
    public DirectExchange DirectExchange(){
        return new DirectExchange("hmall.direct");
    }

    @Bean
    public Queue DirectQueue1(){
        return new Queue("direct.queue1");
    }

    @Bean
    public Binding bindingQueue1(Queue DirectQueue1, DirectExchange DirectExchange){
        return BindingBuilder.bind(DirectQueue1).to(DirectExchange).with("red");
    }

    @Bean
    public Queue DirectQueue2(){
        return new Queue("Direct.queue2");
    }

    @Bean
    public Binding bindingQueue2(Queue DirectQueue2, DirectExchange DirectExchange){
        return BindingBuilder.bind(DirectQueue2).to(DirectExchange).with("yellow");
    }

}
