package org.example.kursach.repository;

import org.example.kursach.domain.WorkflowTask;
import org.example.kursach.domain.WorkflowTaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long> {
    List<WorkflowTask> findByRecordId(Long recordId);
    List<WorkflowTask> findByState(WorkflowTaskState state);
}

