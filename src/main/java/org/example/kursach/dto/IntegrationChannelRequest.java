package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.kursach.domain.DeliveryType;

public record IntegrationChannelRequest(
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 120) String systemName,
        @Size(max = 512) String endpointUrl,
        DeliveryType deliveryType,
        boolean active
) {}

