package com.lazyvic.reflection.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EVENT_EXCHANGE_NAME = "user.event.exchange";
    public static final String RSS_EXCHANGE_NAME = "rss.push.exchange";
    public static final String LOG_QUEUE = "log.queue";
    public static final String BADGE_QUEUE = "badge.queue";
    public static final String RSS_PUSH_QUEUE = "rss.push.queue";

    // DLQ
    public static final String LOG_DLQ = "log.dlq.queue";
    public static final String DLX_EXCHANGE = "log.dlx.exchange";
    public static final String DLQ_ROUTING_KEY= "log.dlq";


    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange logExchange() {
        return new FanoutExchange(EVENT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public FanoutExchange rssPushExchange() {
        return new FanoutExchange(RSS_EXCHANGE_NAME, true, false);
    }

    // ─── Log Queue ────────────────────────────

//    @Bean
//    public Queue logQueue() {
//        return new Queue(LOG_QUEUE, true);
//    }

    @Bean
    public Queue logQueue() {
        return QueueBuilder.durable(LOG_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding logUserEventBinding(FanoutExchange logExchange, Queue logQueue) {
        return BindingBuilder.bind(logQueue).to(logExchange);
    }

    @Bean
    public Binding logRssPushEventBinding(FanoutExchange rssPushExchange, Queue logQueue) {
        return BindingBuilder.bind(logQueue).to(rssPushExchange);
    }

    // ─── RSS Push Queue ───────────────────────

    @Bean Queue rssPushQueue() {
        return new Queue(RSS_PUSH_QUEUE, true);
    }

    @Bean Binding rssPushBinding(FanoutExchange rssPushExchange, Queue rssPushQueue) {
        return BindingBuilder.bind(rssPushQueue).to(rssPushExchange);
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

    // ─── DLQ Exchanger + Queue ──────────────────────────────

    @Bean
    public DirectExchange logDlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue logDlqQueue() {
        return new Queue(LOG_DLQ, true);
    }

    @Bean
    public Binding logDlqBinding(Queue logDlqQueue, DirectExchange logDlxExchange) {
        return BindingBuilder.bind(logDlqQueue)
                .to(logDlxExchange)
                .with(DLQ_ROUTING_KEY);
    }

}
