package com.lazyvic.reflection.events.consumer;

import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.UserActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserBadgeConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserBadgeConsumer.class);



    @RabbitListener(queues = RabbitMqConfig.BADGE_QUEUE)
    public void handleUserAction(UserActionEvent event) {
        log.info("[traceId={}] [UserBadgeConsumer] userId={} | action={} | message=\"{}\" | timestamp={}",
                event.getTraceId(),
                event.getUserId(),
                event.getAction(),
                event.getMessageText(),
                event.getTimestamp()
        );
    }
}