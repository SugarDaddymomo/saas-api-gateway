package com.saasgateway.gateway_service.ratelimit.service;

public interface RateLimitService {
    boolean isAllowed(String tenantId, Integer rateLimit, Integer windowSeconds);
}