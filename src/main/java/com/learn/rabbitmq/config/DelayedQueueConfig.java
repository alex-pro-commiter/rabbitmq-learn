package com.learn.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//x-delayed-message queue config class
@Configuration
public class DelayedQueueConfig {

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_EXCHANGE_TYPE = "x-delayed-message";

    /**
     * config x-delayed-message type exchange with
     * 1 exchange name | String
     * 2 exchange type | String
     * 3 durable | boolean
     * 4 auto delete | boolean
     * 5 other arguments | Map
     **/
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(
                DELAYED_EXCHANGE_NAME,
                DELAYED_EXCHANGE_TYPE,
                true,
                false,
                args);
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
