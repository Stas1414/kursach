package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.kursach.domain.RecordStatus;

public record RecordStatusChangeRequest(
        @NotNull RecordStatus status,
        @NotBlank String actor,
        String comment
) {}

