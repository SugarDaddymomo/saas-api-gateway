package com.saasgateway.gateway_service.analytics.projection;

import java.time.LocalDate;

public interface DailyUsageProjection {
    LocalDate getDate();
    Long getRequestCount();
}