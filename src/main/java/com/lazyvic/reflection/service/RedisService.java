package com.lazyvic.reflection.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testSetGet() {
        redisTemplate.opsForValue().set("testKey", "testValue");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis testKey = " + value);
    }
}
