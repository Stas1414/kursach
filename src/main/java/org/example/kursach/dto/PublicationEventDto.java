package org.example.kursach.dto;

import org.example.kursach.domain.PublicationEvent;

import java.time.Instant;

public record PublicationEventDto(
        Long id,
        Long recordId,
        Long channelId,
        PublicationEvent.Status status,
        String message,
        Instant createdAt,
        Instant finishedAt
) {}

