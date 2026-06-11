package com.saasgateway.gateway_service.route.service;

import java.util.Optional;
import java.util.UUID;
import com.saasgateway.gateway_service.route.entity.Route;

public interface RouteLookupService {

    Optional<Route> findRoute(UUID tenantId, String path, String method);
}