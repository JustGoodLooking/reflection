package com.lazyvic.reflection.events.producer;

import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.RssPushEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RssPushEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public RssPushEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(List<RssPushEvent> events) {
        events.forEach(event ->
                rabbitTemplate.convertAndSend(RabbitMqConfig.RSS_EXCHANGE_NAME, "", event)
        );

        System.out.println("Rss push event produce sent event to MQ... ");
    }
}
