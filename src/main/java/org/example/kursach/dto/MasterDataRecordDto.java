package org.example.kursach.dto;

import org.example.kursach.domain.RecordStatus;

import java.time.Instant;

public record MasterDataRecordDto(
        Long id,
        Long domainId,
        String domainCode,
        String businessKey,
        String payloadJson,
        RecordStatus status,
        String sourceSystem,
        String owner,
        int versionNumber,
        Instant createdAt,
        Instant updatedAt,
        Instant publishedAt
) {}

