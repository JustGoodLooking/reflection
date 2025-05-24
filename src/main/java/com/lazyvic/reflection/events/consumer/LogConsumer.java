package com.lazyvic.reflection.events.consumer;


import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.UserActionEvent;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LogConsumer {
    private static final Logger log = LoggerFactory.getLogger(LogConsumer.class);
    private final Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();


    @RabbitListener(queues = RabbitMqConfig.LOG_QUEUE, ackMode = "MANUAL")
    public void handleEventAction(UserActionEvent event, Channel channel, Message message) throws RuntimeException, IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String traceId = event.getTraceId();
        String messageText = event.getMessageText();
        log.info("Receive user event traceId={} | message={}", traceId, messageText);
        try {
            if (messageText.toLowerCase().contains("boom")) {
                throw new RuntimeException("Boom triggered for traceId=" + traceId);
            }
            doLog(event);

            channel.basicAck(deliveryTag, false);
            log.info("Ack success traceId={}", traceId);

        } catch (Exception e) {
            log.error("An error occurred, traceId={}，message will be requeued: {}", traceId, e.getMessage());

            int retryCount = retryCountMap.getOrDefault(traceId, 0);

            if (retryCount >= 2) {
                log.warn("Retry limit reached for traceId={}，no longer requeuing", traceId);
                retryCountMap.remove(traceId);
                channel.basicNack(deliveryTag, false, false); // 不再重送
            } else {
                retryCountMap.put(traceId, retryCount + 1);
                log.warn("Retry #{} for traceId={}, will requeue", retryCount + 1, traceId);
                channel.basicNack(deliveryTag, false, true); // 再次重送
            }
        }

    }

//    @RabbitListener(queues = RabbitMqConfig.LOG_QUEUE, ackMode = "MANUAL")
//    public void handleRssAction(RssPushEvent event, Channel channel, Message message) throws RuntimeException, IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        String traceId = event.getTraceId();
//        String messageText = event.getLink();
//        log.info("Receive rss push event traceId={} | message={}", traceId, messageText);
//        try {
//            doPushLog(event);
//
//            channel.basicAck(deliveryTag, false);
//            log.info("Ack success traceId={}", traceId);
//
//        } catch (Exception e) {
//            log.warn("Retry limit reached for traceId={}，no longer requeuing", traceId);
//            retryCountMap.remove(traceId);
//            channel.basicNack(deliveryTag, false, false); // 不再重送
//
//        }
//
//    }

    private void doLog(UserActionEvent event) {
        log.info("[traceId={}] [LogConsumer] userId={} | action={} | message=\"{}\" | timestamp={}",
                event.getTraceId(),
                event.getUserId(),
                event.getAction(),
                event.getMessageText(),
                event.getTimestamp()
        );
    }

//    private void doPushLog(RssPushEvent event) {
//        log.info("[traceId={}] [LogConsumer] userId={} | link={}",
//                event.getTraceId(),
//                event.getUserId(),
//                event.getLink()
//        );
//    }

}