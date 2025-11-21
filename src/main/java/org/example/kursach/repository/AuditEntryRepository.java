package org.example.kursach.repository;

import org.example.kursach.domain.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditEntryRepository extends JpaRepository<AuditEntry, Long> {
    List<AuditEntry> findTop20ByOrderByCreatedAtDesc();
    List<AuditEntry> findByRecordIdOrderByCreatedAtDesc(Long recordId);
}

