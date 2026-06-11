package com.saasgateway.admin.route.dto;

import com.saasgateway.admin.route.entity.RouteStatus;

public record RouteSearchRequest(
        String search,
        RouteStatus status,
        String sortBy,
        String sortDirection,
        Integer page,
        Integer size
) {
}