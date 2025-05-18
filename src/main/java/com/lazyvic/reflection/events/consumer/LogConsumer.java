package com.lazyvic.reflection.events.consumer;


import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.UserActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {
    private static final Logger log = LoggerFactory.getLogger(LogConsumer.class);



    @RabbitListener(queues = RabbitMqConfig.LOG_QUEUE)
    public void handleUserAction(UserActionEvent event) {
        log.info("[traceId={}] [LogConsumer] userId={} | action={} | message=\"{}\" | timestamp={}",
                event.getTraceId(),
                event.getUserId(),
                event.getAction(),
                event.getMessageText(),
                event.getTimestamp()
        );
    }
}