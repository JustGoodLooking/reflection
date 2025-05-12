package com.lazyvic.reflection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedisServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisService redisService;

    @BeforeEach
    void setUp() {
        redisService = new RedisService(redisTemplate);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldReturnFalseWhenKeyExists() {
        String expectedKey = "remind:daily:123:" + LocalDate.now();
        when(redisTemplate.hasKey(expectedKey)).thenReturn(true);

        boolean result = redisService.shouldRemind(123L, "daily");

        assertFalse(result);
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void shouldReturnTrueAndSetKeyWhenKeyNotExists() {
        String expectedKey = "remind:daily:123:" + LocalDate.now();
        when(redisTemplate.hasKey(expectedKey)).thenReturn(false);

        boolean result = redisService.shouldRemind(123L, "daily");

        assertTrue(result);
        verify(valueOperations).set(eq(expectedKey), eq("1"), eq(Duration.ofHours(24)));
    }
}