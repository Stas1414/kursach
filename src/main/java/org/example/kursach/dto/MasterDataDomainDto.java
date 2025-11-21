package org.example.kursach.dto;

import java.time.Instant;

public record MasterDataDomainDto(
        Long id,
        String code,
        String name,
        String description,
        String steward,
        boolean active,
        double qualityScore,
        Instant createdAt,
        Instant updatedAt
) {}

