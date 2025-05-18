package com.lazyvic.reflection.events.producer;


import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.UserActionEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserActionEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public UserActionEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(UserActionEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "", event);
        System.out.println("Sent event to MQ: " + event);
    }
}