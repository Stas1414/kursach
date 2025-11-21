package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.kursach.domain.WorkflowTaskState;

public record WorkflowTaskStateChangeRequest(
        @NotNull WorkflowTaskState state,
        @NotBlank String actor,
        String comment
) {}

