package com.saasgateway.gateway_service.analytics.projection;

public interface RouteUsageProjection {
    String getPath();
    Long getRequestCount();
}