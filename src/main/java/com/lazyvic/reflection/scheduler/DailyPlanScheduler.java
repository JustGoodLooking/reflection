package com.lazyvic.reflection.scheduler;

import com.lazyvic.reflection.service.RedisService;
import com.lazyvic.reflection.telegrambot.RefectionBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DailyPlanScheduler {
    private RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(DailyPlanScheduler.class);
    private final AtomicInteger failureCounter = new AtomicInteger(0);
    private final RefectionBot refectionBot;

    public DailyPlanScheduler(RedisService redisService, RefectionBot refectionBot) {
        this.redisService = redisService;
        this.refectionBot = refectionBot;
    }
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendDailyPlansReminders() {
        if (!redisService.shouldRemind(6653324577L, "daily")) {
            return;
        }

        int count = failureCounter.incrementAndGet(); // Increment execution count

        // Simulate failure every 3rd time
        if (count % 3 == 0) {
            try {
                throw new RuntimeException("Intentional failure, " + count + "th execution");
            } catch (RuntimeException e) {
                // Log the failure message
                logger.error("Scheduler failed, this is the {} time to execute, error message: {}", count, e.getMessage());
            }
        } else {

            // Normal execution
            refectionBot.sendMessageToUser(6653324577L, "test");

            // Log the success message
            logger.info("Scheduler succeeded, this is the {} time to execute, successfully sent the message", count);
        }
    }

}
