package com.saasgateway.gateway_service.route.cache;

import java.util.Optional;
import java.util.UUID;
import com.saasgateway.gateway_service.route.entity.Route;

public interface RouteCacheService {
    
    Optional<CachedRoute> get(UUID tenantId, String path, String method);
    void put(UUID tenantId, String path, String method, CachedRoute route);
}