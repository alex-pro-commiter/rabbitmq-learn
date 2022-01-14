package com.learn.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//ttl队列配置文件
@Configuration
public class ttlConfig {
    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A= "QA";
    public static final String QUEUE_B= "QB";
    public static final String DEAD_LETTER_QUEUE= "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        return QueueBuilder
                .durable(QUEUE_A)
                .ttl(10000)
                .deadLetterExchange(DEAD_LETTER_QUEUE)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean("queueB")
    public Queue queueB(){
        return QueueBuilder
                .durable(QUEUE_B)
                .ttl(40000)
                .deadLetterExchange(DEAD_LETTER_QUEUE)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                    @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
