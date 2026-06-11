package com.saasgateway.admin.common.exception;

import java.util.UUID;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(UUID routeId) {
        super("Route not found: " + routeId);
    }
}