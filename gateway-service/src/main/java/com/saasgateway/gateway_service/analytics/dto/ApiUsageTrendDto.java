package com.saasgateway.gateway_service.analytics.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ApiUsageTrendDto(
        LocalDate date,
        long requestCount
) {

}