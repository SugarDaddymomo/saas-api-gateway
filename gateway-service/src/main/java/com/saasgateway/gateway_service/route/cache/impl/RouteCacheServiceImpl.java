package com.saasgateway.gateway_service.route.cache.impl;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saasgateway.gateway_service.route.cache.CachedRoute;
import com.saasgateway.gateway_service.route.cache.RouteCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteCacheServiceImpl implements RouteCacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<CachedRoute> get(UUID tenantId, String path, String method) {

        try {
            String json = redisTemplate.opsForValue().get(key(tenantId, path, method));
            if (json == null) {
                return Optional.empty();
            }
            CachedRoute route = objectMapper.readValue(json,CachedRoute.class);
            return Optional.of(route);
        } catch (Exception ex) {
            String cacheKey = key(tenantId, path, method);
            redisTemplate.delete(cacheKey);
            log.warn("Invalid cache entry removed: {}", cacheKey);
            return Optional.empty();
        }
    }

    @Override
    public void put(UUID tenantId, String path, String method, CachedRoute route) {

        try {
            String json = objectMapper.writeValueAsString(route);

            redisTemplate.opsForValue().set(key(tenantId, path, method), json, Duration.ofMinutes(30));
        } catch (Exception ex) {
            log.error("Failed to write route to cache", ex);
        }
    }

    private String key(UUID tenantId, String path, String method) {
        return "route:" + tenantId + ":" + method + ":" + path;
    }

}