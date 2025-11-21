package org.example.kursach.dto;

import org.example.kursach.domain.IssueSeverity;
import org.example.kursach.domain.IssueStatus;

import java.time.Instant;

public record DataQualityIssueDto(
        Long id,
        Long recordId,
        String ruleName,
        IssueSeverity severity,
        IssueStatus status,
        String details,
        String resolutionComment,
        Instant detectedAt,
        Instant resolvedAt
) {}

