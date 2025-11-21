package org.example.kursach.dto;

import org.example.kursach.domain.WorkflowTaskState;
import org.example.kursach.domain.WorkflowTaskType;

import java.time.Instant;

public record WorkflowTaskDto(
        Long id,
        Long recordId,
        WorkflowTaskType type,
        WorkflowTaskState state,
        String assignee,
        Instant dueDate,
        String comment,
        Instant createdAt,
        Instant completedAt
) {}

