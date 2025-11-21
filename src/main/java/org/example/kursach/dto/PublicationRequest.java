package org.example.kursach.dto;

import jakarta.validation.constraints.NotNull;

public record PublicationRequest(
        @NotNull Long recordId,
        @NotNull Long channelId,
        String actor
) {}

