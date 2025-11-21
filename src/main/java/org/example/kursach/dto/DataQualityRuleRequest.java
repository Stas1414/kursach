package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.kursach.domain.IssueSeverity;

public record DataQualityRuleRequest(
        @NotNull Long domainId,
        @NotBlank @Size(max = 128) String name,
        @Size(max = 1000) String description,
        @NotBlank @Size(max = 1024) String expression,
        @NotNull IssueSeverity severity,
        boolean blocking
) {}

