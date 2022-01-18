package com.learn.rabbitmq.controller;

import com.learn.rabbitmq.config.DelayedQueueConfig;
import com.learn.rabbitmq.config.ttlConfig;
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

    /**
     * send message to two queue based on configuration ttl time
     */

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("current time is:{},send message to two ttl message queue {}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend(ttlConfig.X_EXCHANGE, "XA", "message from ttl 10s queue" + msg);
        rabbitTemplate.convertAndSend(ttlConfig.X_EXCHANGE, "XB", "message from ttl 20s queue" + msg);
    }

    /**
     * send message to one queue based on argument ttl time
     * you will see the result in console print the queue is FIFO not based ttl
     */

    @GetMapping("/sendTtlMsg/{msg}/{ttl}")
    public void sendTtlMsg(@PathVariable String msg,
                           @PathVariable String ttl) {
        log.info("current time:{},send message ttl {} to queue C : {}",
                new Date().toString(), ttl, msg);
        rabbitTemplate.convertAndSend(ttlConfig.X_EXCHANGE, "XC", "message from queue C:" + msg, message -> {
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });
    }

    /**
     * send message to one delayed queue based on rabbit plugin "rabbitmq_delayed_message_exchange"
     * you will see the result in console print the queue is based the delayed argument lower is first
     */

    @GetMapping("/sendDelayedMsg/{msg}/{delayTime}")
    public void sendDelayedMsg(@PathVariable String msg,
                               @PathVariable Integer delayTime) {
        log.info("current time:{},send delayed {} message to delayed queue :{} ",
                new Date().toString(), delayTime, msg);

        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                msg,
                message -> {
                    message.getMessageProperties().setDelay(delayTime);
                    return message;
                });
    }
}
