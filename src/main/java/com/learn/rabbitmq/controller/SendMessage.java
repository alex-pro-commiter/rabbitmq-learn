package com.learn.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessage {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SendMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("current time is:{},send message to two ttl message queue {}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "message from ttl 10s queue" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "message from ttl 40s queue" + msg);
    }
}
