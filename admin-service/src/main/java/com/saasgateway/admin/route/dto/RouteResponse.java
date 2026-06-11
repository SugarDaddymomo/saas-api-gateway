package com.saasgateway.admin.route.dto;

import java.util.UUID;
import com.saasgateway.admin.route.entity.RouteMethod;
import com.saasgateway.admin.route.entity.RouteStatus;

public record RouteResponse(
        UUID id,
        String name,
        String pathPattern,
        String targetUrl,
        RouteMethod method,
        RouteStatus status
) {
}