package org.example.kursach.repository;

import org.example.kursach.domain.DataQualityIssue;
import org.example.kursach.domain.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataQualityIssueRepository extends JpaRepository<DataQualityIssue, Long> {
    List<DataQualityIssue> findByRecordId(Long recordId);
    long countByStatus(IssueStatus status);
}

