package com.saasgateway.admin.route.dto;

import com.saasgateway.admin.route.entity.RouteMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRouteRequest(
        @NotBlank
        String name,
        @NotBlank
        String pathPattern,
        @NotBlank
        String targetUrl,
        @NotNull
        RouteMethod method
) {
}