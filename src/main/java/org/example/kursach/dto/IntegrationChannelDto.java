package org.example.kursach.dto;

import org.example.kursach.domain.DeliveryType;

import java.time.Instant;

public record IntegrationChannelDto(
        Long id,
        String code,
        String systemName,
        String endpointUrl,
        DeliveryType deliveryType,
        boolean active,
        Instant lastPublishedAt,
        Instant createdAt,
        Instant updatedAt
) {}

