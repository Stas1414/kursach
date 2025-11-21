package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MasterDataDomainRequest(
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 255) String name,
        @Size(max = 2000) String description,
        @Size(max = 120) String steward,
        boolean active,
        double qualityScore
) {}

