package com.lazyvic.reflection.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class RedisService {
    private StringRedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testSetGet() {
        redisTemplate.opsForValue().set("testKey", "testValue");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis testKey = " + value);
    }

    public boolean shouldRemind(Long userId, String tag) {
        String key = "remind: " + tag + ":" + userId + ":" + LocalDate.now();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            logger.info("already has key, skip..");
            return false;
        }
        logger.info("set key for today's reminder....");
        redisTemplate.opsForValue().set(key, "1", Duration.ofHours(24));
        return true;
    }
}
