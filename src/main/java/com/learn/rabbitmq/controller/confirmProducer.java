package com.learn.rabbitmq.controller;

import com.learn.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/confirm")
public class confirmProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public confirmProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}")
    public void sendConfirmMessage(@PathVariable String msg) {
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                msg,
                correlationData);
        log.info("send message :{}", msg);
    }
}
