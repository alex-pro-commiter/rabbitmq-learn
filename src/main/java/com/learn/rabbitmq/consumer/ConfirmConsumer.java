package com.learn.rabbitmq.consumer;

import com.learn.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void confirmReceive(Message msg) {
        String m = new String(msg.getBody());
        log.info("received confirm queue message: {}", m);
    }
}
/**
 * this block defined a inner class implements confirmCallback functional interface
 * and override the confirm interface.
 * if message cannot send to exchange,
 * the callback functional can deal with the returned ack signal and message defined id
 */
@Slf4j
@Component
class confirmCallBack implements RabbitTemplate.ConfirmCallback {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public confirmCallBack(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String failReason) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("exchange received id: {}", id);
        } else
            log.info("exchange discard message id: {}", id);
    }
}
