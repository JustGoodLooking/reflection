package com.lazyvic.reflection.events.consumer;

import com.lazyvic.reflection.config.RabbitMqConfig;
import com.lazyvic.reflection.events.model.RssPushEvent;
import com.lazyvic.reflection.service.RedisService;
import com.lazyvic.reflection.telegrambot.RefectionBot;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RssPushConsumer {
    private final RefectionBot refectionBot;
    private final RedisService redisService;

    public RssPushConsumer(RefectionBot refectionBot, RedisService redisService) {
        this.refectionBot = refectionBot;
        this.redisService = redisService;
    }

    @RabbitListener(queues = RabbitMqConfig.RSS_PUSH_QUEUE)
    public void handleRssPush(RssPushEvent event) {
        String msg = event.getTitle() + "\n" + event.getLink();
        refectionBot.sendMessageToUser(event.getUserId(), msg);
        redisService.markNewsAsSent(event.getUserId(), event.getLink());

    }
}
