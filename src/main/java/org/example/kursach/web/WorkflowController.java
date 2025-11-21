package org.example.kursach.web;

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
public class WorkflowController {

    private final WorkflowService service;

    public WorkflowController(WorkflowService service) {
        this.service = service;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkflowTaskDto createTask(@Valid @RequestBody WorkflowTaskRequest request) {
        return service.createTask(request);
    }

    @PostMapping("/tasks/{taskId}/state")
    public WorkflowTaskDto changeState(@PathVariable Long taskId,
                                       @Valid @RequestBody WorkflowTaskStateChangeRequest request) {
        return service.changeState(taskId, request);
    }

    @GetMapping("/tasks/record/{recordId}")
    public List<WorkflowTaskDto> tasksForRecord(@PathVariable Long recordId) {
        return service.tasksForRecord(recordId);
    }

    @GetMapping("/tasks")
    public List<WorkflowTaskDto> tasksByState(@RequestParam WorkflowTaskState state) {
        return service.tasksByState(state);
    }
}

