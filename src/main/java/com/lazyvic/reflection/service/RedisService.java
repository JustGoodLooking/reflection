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
        String key = "remind:" + tag + ":" + userId + ":" + LocalDate.now();
        // 原子操作：只有當 key 不存在時才會設值成功
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofHours(24));

        if (Boolean.TRUE.equals(success)) {
            logger.info("Set reminder key [{}] successfully, will proceed to push message.", key);
            return true;
        } else {
            logger.info("Reminder key [{}] already exists, skip pushing message.", key);
            return false;
        }
    }
}
