package com.saasgateway.admin.route.specification;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.saasgateway.admin.route.entity.Route;
import com.saasgateway.admin.route.entity.RouteStatus;

public class RouteSpecification {

    public static Specification<Route> belongsToTenant(UUID tenantId) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("tenant").get("id"),
                        tenantId);
    }

    public static Specification<Route> hasName(String search) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%");
    }

    public static Specification<Route> hasStatus(RouteStatus status) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status);
    }
}