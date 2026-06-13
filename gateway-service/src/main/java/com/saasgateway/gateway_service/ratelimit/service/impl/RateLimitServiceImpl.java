package com.saasgateway.gateway_service.ratelimit.service.impl;

import java.time.Duration;
import java.time.Instant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.saasgateway.gateway_service.ratelimit.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitServiceImpl implements RateLimitService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean isAllowed(String tenantId, Integer rateLimit, Integer windowSeconds) {
        int limit = rateLimit != null ? rateLimit : 100;
        int window = windowSeconds != null ? windowSeconds : 60;
        String key = "rate_limit:" + tenantId + ":"
            + (
                Instant.now().getEpochSecond()
                / window
        );
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(window));
        }
        log.info("Tenant={} Count={} Limit={} Window={}", tenantId, count, limit, window);
        return count != null && count <= limit;
    }
}