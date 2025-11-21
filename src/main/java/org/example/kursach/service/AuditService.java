package org.example.kursach.service;

import org.example.kursach.domain.AuditEntry;
import org.example.kursach.domain.MasterDataRecord;
import org.example.kursach.dto.AuditEntryDto;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.AuditEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final AuditEntryRepository repository;

    public AuditService(AuditEntryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void log(MasterDataRecord record, AuditEntry.ActionType type, String actor, String message) {
        AuditEntry entry = new AuditEntry();
        entry.setRecord(record);
        entry.setActionType(type);
        entry.setActor(actor != null ? actor : "system");
        entry.setMessage(message);
        repository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDto> latest() {
        return repository.findTop20ByOrderByCreatedAtDesc().stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDto> byRecord(Long recordId) {
        return repository.findByRecordIdOrderByCreatedAtDesc(recordId).stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }
}

