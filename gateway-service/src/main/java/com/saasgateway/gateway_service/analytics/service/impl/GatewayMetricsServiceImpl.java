package com.saasgateway.gateway_service.analytics.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.saasgateway.gateway_service.analytics.dto.GatewayMetricsDto;
import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayMetricsServiceImpl implements GatewayMetricsService {

    private static final String RATE_LIMITED_KEY = "gateway:rate_limited_requests";
    private static final String RPM_KEY_PREFIX = "gateway:rpm:";
    private static final String CACHE_HITS_KEY = "gateway:cache_hits";
    private static final String CACHE_MISSES_KEY = "gateway:cache_misses";
    private static final String TENANT_RATE_LIMITED_PREFIX = "gateway:tenant_rate_limited:";
    private final StringRedisTemplate redisTemplate;
    
    @Override
    public long getRateLimitedRequests() {
        String value = redisTemplate.opsForValue().get(RATE_LIMITED_KEY);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    @Override
    public void incrementRateLimitedRequests() {
        redisTemplate.opsForValue().increment(RATE_LIMITED_KEY);
    }

    @Override
    public long getCurrentRequestsPerMinute() {
        long bucket = Instant.now().getEpochSecond() / 60;
        String key = RPM_KEY_PREFIX + bucket;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    @Override
    public void incrementRequestsPerMinute() {
        long bucket = Instant.now().getEpochSecond() / 60;
        String key = RPM_KEY_PREFIX + bucket;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key,Duration.ofMinutes(2));
        }
    }

    @Override
    public void incrementCacheHits() {
        redisTemplate.opsForValue().increment(CACHE_HITS_KEY);
    }

    @Override
    public void incrementCacheMisses() {
        redisTemplate.opsForValue().increment(CACHE_MISSES_KEY);
    }

    @Override
    public long getCacheHits() {
        String value = redisTemplate.opsForValue().get(CACHE_HITS_KEY);
        return value == null ? 0 : Long.parseLong(value);
    }

    @Override
    public long getCacheMisses() {
        String value = redisTemplate.opsForValue().get(CACHE_MISSES_KEY);
        return value == null ? 0 : Long.parseLong(value);
    }

    @Override
    public GatewayMetricsDto getMetrics() {
        return GatewayMetricsDto.builder()
                .rateLimitedRequests(getRateLimitedRequests())
                .requestsPerMinute(getCurrentRequestsPerMinute())
                .cacheHits(getCacheHits())
                .cacheMisses(getCacheMisses())
                .totalCachedRoutes(getTotalCachedRoutes())
                .build();
    }

    @Override
    public long getTotalCachedRoutes() {
        Set<String> keys = redisTemplate.keys("route:*");
        if (keys == null) {
            return 0;
        }
        return keys.size();
    }

    @Override
    public void incrementTenantRateLimited(UUID tenantId) {
        String key = TENANT_RATE_LIMITED_PREFIX + tenantId;
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Map<UUID, Long> getTenantRateLimitedRequests() {
        Set<String> keys = redisTemplate.keys("gateway:tenant_rate_limited:*");
        if (keys == null || keys.isEmpty()) {
            return Map.of();
        }
        Map<UUID, Long> result = new HashMap<>();
        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                continue;
            }
            String tenantId = key.substring("gateway:tenant_rate_limited:".length());
            result.put(UUID.fromString(tenantId), Long.parseLong(value));
        }
        return result;
    }

}