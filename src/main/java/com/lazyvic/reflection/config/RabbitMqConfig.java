package com.lazyvic.reflection.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE_NAME = "user.event.exchange";
    public static final String LOG_QUEUE = "log.queue";
    public static final String BADGE_QUEUE = "badge.queue";


    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange logExchange() {
        return new FanoutExchange(EXCHANGE_NAME, true, false);
    }


    // ─── Log Queue ────────────────────────────

    @Bean
    public Queue logQueue() {
        return new Queue(LOG_QUEUE, true);
    }

    @Bean
    public Binding logBinding(FanoutExchange logExchange, Queue logQueue) {
        return BindingBuilder.bind(logQueue).to(logExchange);
    }

    // ─── Badge Queue ──────────────────────────

    @Bean
    public Queue badgeQueue() {
        return new Queue(BADGE_QUEUE, true);
    }

    @Bean
    public Binding badgeBinding(FanoutExchange logExchange, Queue badgeQueue) {
        return BindingBuilder.bind(badgeQueue).to(logExchange);
    }
}
