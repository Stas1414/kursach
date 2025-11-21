package org.example.kursach.dto;

import org.example.kursach.domain.AuditEntry;

import java.time.Instant;

public record AuditEntryDto(
        Long id,
        Long recordId,
        AuditEntry.ActionType actionType,
        String actor,
        String message,
        Instant createdAt
) {}

