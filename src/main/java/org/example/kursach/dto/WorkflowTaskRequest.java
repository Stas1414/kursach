package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.kursach.domain.WorkflowTaskType;

import java.time.Instant;

public record WorkflowTaskRequest(
        @NotNull Long recordId,
        @NotNull WorkflowTaskType type,
        @NotBlank String assignee,
        Instant dueDate,
        String comment
) {}

