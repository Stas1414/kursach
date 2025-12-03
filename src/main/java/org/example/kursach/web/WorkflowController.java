package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.domain.WorkflowTaskState;
import org.example.kursach.dto.WorkflowTaskDto;
import org.example.kursach.dto.WorkflowTaskRequest;
import org.example.kursach.dto.WorkflowTaskStateChangeRequest;
import org.example.kursach.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflow")
@Tag(name = "Workflow", description = "Задачи согласования изменений мастер‑данных.")
public class WorkflowController {

    private final WorkflowService service;

    public WorkflowController(WorkflowService service) {
        this.service = service;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать задачу workflow", description = "Создаёт задачу на ревью/утверждение/публикацию мастер‑записи.")
    public WorkflowTaskDto createTask(@Valid @RequestBody WorkflowTaskRequest request) {
        return service.createTask(request);
    }

    @PostMapping("/tasks/{taskId}/state")
    @Operation(summary = "Изменить состояние задачи", description = "Переводит задачу workflow в новый статус (OPEN/IN_PROGRESS/COMPLETED/...).")
    public WorkflowTaskDto changeState(@PathVariable Long taskId,
                                       @Valid @RequestBody WorkflowTaskStateChangeRequest request) {
        return service.changeState(taskId, request);
    }

    @GetMapping("/tasks/record/{recordId}")
    @Operation(summary = "Задачи по мастер‑записи", description = "Возвращает задачи согласования для указанной мастер‑записи.")
    public List<WorkflowTaskDto> tasksForRecord(@PathVariable Long recordId) {
        return service.tasksForRecord(recordId);
    }

    @GetMapping("/tasks")
    @Operation(summary = "Задачи по статусу", description = "Возвращает задачи workflow с указанным статусом.")
    public List<WorkflowTaskDto> tasksByState(@RequestParam WorkflowTaskState state) {
        return service.tasksByState(state);
    }
}

