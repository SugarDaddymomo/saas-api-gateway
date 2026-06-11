package com.saasgateway.admin.route.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import com.saasgateway.admin.route.dto.CreateRouteRequest;
import com.saasgateway.admin.route.dto.CreateRouteResponse;
import com.saasgateway.admin.route.dto.RouteResponse;
import com.saasgateway.admin.route.dto.RouteSearchRequest;
import com.saasgateway.admin.tenant.dto.MessageResponse;

public interface RouteService {

    CreateRouteResponse createRoute(UUID tenantId, CreateRouteRequest request);

    Page<RouteResponse> getRoutes(UUID tenantId, RouteSearchRequest request);

    RouteResponse getRoute(UUID routeId);

    RouteResponse updateRoute(UUID routeId, CreateRouteRequest request);

    MessageResponse enableRoute(UUID routeId);

    MessageResponse disableRoute(UUID routeId);
}