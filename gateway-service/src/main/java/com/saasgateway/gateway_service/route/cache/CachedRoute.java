package com.saasgateway.gateway_service.route.cache;

import java.io.Serializable;
import java.util.UUID;
import com.saasgateway.gateway_service.route.entity.RouteMethod;

public record CachedRoute(
    UUID id,
    String name,
    String pathPattern,
    String targetUrl,
    RouteMethod method
) implements Serializable {
    
}