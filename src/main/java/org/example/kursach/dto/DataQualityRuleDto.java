package org.example.kursach.dto;

import org.example.kursach.domain.IssueSeverity;

import java.time.Instant;

public record DataQualityRuleDto(
        Long id,
        Long domainId,
        String name,
        String description,
        String expression,
        IssueSeverity severity,
        boolean blocking,
        Instant createdAt,
        Instant updatedAt
) {}

