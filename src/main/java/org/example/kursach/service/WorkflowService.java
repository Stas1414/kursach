package org.example.kursach.service;

import org.example.kursach.domain.AuditEntry;
import org.example.kursach.domain.MasterDataRecord;
import org.example.kursach.domain.WorkflowTask;
import org.example.kursach.domain.WorkflowTaskState;
import org.example.kursach.dto.WorkflowTaskDto;
import org.example.kursach.dto.WorkflowTaskRequest;
import org.example.kursach.dto.WorkflowTaskStateChangeRequest;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.example.kursach.repository.WorkflowTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class WorkflowService {

    private final WorkflowTaskRepository taskRepository;
    private final MasterDataRecordRepository recordRepository;
    private final AuditService auditService;

    public WorkflowService(WorkflowTaskRepository taskRepository,
                           MasterDataRecordRepository recordRepository,
                           AuditService auditService) {
        this.taskRepository = taskRepository;
        this.recordRepository = recordRepository;
        this.auditService = auditService;
    }

    @Transactional
    public WorkflowTaskDto createTask(WorkflowTaskRequest request) {
        MasterDataRecord record = recordRepository.findById(request.recordId())
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(request.recordId())));
        WorkflowTask task = new WorkflowTask();
        task.setRecord(record);
        task.setType(request.type());
        task.setAssignee(request.assignee());
        task.setDueDate(request.dueDate());
        task.setComment(request.comment());
        WorkflowTask saved = taskRepository.save(task);
        auditService.log(record, AuditEntry.ActionType.UPDATE, request.assignee(),
                "Назначена новая задача %s".formatted(request.type()));
        return MasterDataMapper.toDto(saved);
    }

    @Transactional
    public WorkflowTaskDto changeState(Long taskId, WorkflowTaskStateChangeRequest request) {
        WorkflowTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task %d not found".formatted(taskId)));
        task.setState(request.state());
        task.setComment(request.comment());
        if (request.state() == WorkflowTaskState.COMPLETED) {
            task.setCompletedAt(Instant.now());
        }
        auditService.log(task.getRecord(), AuditEntry.ActionType.UPDATE, request.actor(),
                "Задача %d изменена на %s".formatted(task.getId(), task.getState()));
        return MasterDataMapper.toDto(task);
    }

    @Transactional(readOnly = true)
    public List<WorkflowTaskDto> tasksForRecord(Long recordId) {
        return taskRepository.findByRecordId(recordId).stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WorkflowTaskDto> tasksByState(WorkflowTaskState state) {
        return taskRepository.findByState(state).stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }
}

