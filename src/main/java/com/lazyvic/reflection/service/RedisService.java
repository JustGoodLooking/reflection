package com.lazyvic.reflection.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private StringRedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
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

    public boolean allowAdd(String action, Long userId, int maxPerMinute) {
        String key = String.format("ratelimit:%s:%d", action, userId);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        return count <= maxPerMinute;
    }

    public boolean isNewsAlreadySent(Long userId, String link) {
        String key = "rss:sent:" + userId + ":" + hash(link);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void markNewsAsSent(Long userId, String link) {
        String key = "rss:sent:" + userId + ":" + hash(link);
        redisTemplate.opsForValue().set(key, "1", Duration.ofDays(2)); // TTL 2 天
    }

    public boolean isAskCooldownActive(Long userId) {
        String key = "rss:ask:" + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void activateAskCoolDown(Long userId) {
        String key = "rss:ask:" + userId;
        redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(30));
    }

    public long getCooldownRemainingSeconds(Long userId) {
        String key = "rss:ask:" + userId;
        Long seconds = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return (seconds != null && seconds > 0) ? seconds : 0;
    }

    public String getCachedRssString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void cacheRssString(String key, String value, int seconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

    private String hash(String input) {
        return Integer.toHexString(input.hashCode());
    }


}
