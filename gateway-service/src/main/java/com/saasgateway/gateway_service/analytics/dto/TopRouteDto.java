package com.saasgateway.gateway_service.analytics.dto;

import lombok.Builder;

@Builder
public record TopRouteDto(
        String path,
        long requestCount
) {
    
}