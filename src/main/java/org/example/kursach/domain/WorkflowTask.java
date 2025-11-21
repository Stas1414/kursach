package org.example.kursach.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "mdm_workflow_tasks")
public class WorkflowTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "record_id")
    private MasterDataRecord record;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WorkflowTaskType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WorkflowTaskState state = WorkflowTaskState.OPEN;

    @Column(length = 120)
    private String assignee;

    private Instant dueDate;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant completedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public MasterDataRecord getRecord() {
        return record;
    }

    public void setRecord(MasterDataRecord record) {
        this.record = record;
    }

    public WorkflowTaskType getType() {
        return type;
    }

    public void setType(WorkflowTaskType type) {
        this.type = type;
    }

    public WorkflowTaskState getState() {
        return state;
    }

    public void setState(WorkflowTaskState state) {
        this.state = state;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}

