package com.learn.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * config for advance publish confirm mode
 * if message cannot publish success
 * message will be portal to cache
 * then async task will publish again
 */

@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "confirm_key";

    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange,
                                                      @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
