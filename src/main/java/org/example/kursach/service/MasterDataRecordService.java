package org.example.kursach.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kursach.domain.AuditEntry;
import org.example.kursach.domain.MasterDataDomain;
import org.example.kursach.domain.MasterDataRecord;
import org.example.kursach.domain.RecordStatus;
import org.example.kursach.dto.*;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MasterDataRecordService {

    private final MasterDataRecordRepository recordRepository;
    private final MasterDataDomainRepository domainRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public MasterDataRecordService(MasterDataRecordRepository recordRepository,
                                   MasterDataDomainRepository domainRepository,
                                   AuditService auditService,
                                   ObjectMapper objectMapper) {
        this.recordRepository = recordRepository;
        this.domainRepository = domainRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<MasterDataRecordDto> search(RecordSearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.page(), request.size());
        Page<MasterDataRecord> page = recordRepository.search(
                request.domainId(),
                request.status(),
                request.owner(),
                pageRequest
        );
        return new PageResponse<>(
                page.getContent().stream().map(MasterDataMapper::toDto).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public MasterDataRecordDto get(Long id) {
        return recordRepository.findById(id)
                .map(MasterDataMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(id)));
    }

    @Transactional
    public MasterDataRecordDto create(MasterDataRecordRequest request) {
        MasterDataDomain domain = domainRepository.findById(request.domainId())
                .orElseThrow(() -> new NotFoundException("Domain %d not found".formatted(request.domainId())));
        validatePayload(request.payloadJson());
        MasterDataRecord record = new MasterDataRecord();
        record.setDomain(domain);
        record.setBusinessKey(request.businessKey());
        record.setPayloadJson(request.payloadJson());
        record.setSourceSystem(request.sourceSystem());
        record.setOwner(request.owner());
        MasterDataRecord saved = recordRepository.save(record);
        auditService.log(saved, AuditEntry.ActionType.CREATE, request.owner(), "Создана запись мастер-данных");
        return MasterDataMapper.toDto(saved);
    }

    @Transactional
    public MasterDataRecordDto update(Long id, MasterDataRecordRequest request) {
        MasterDataRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(id)));
        if (record.getStatus() == RecordStatus.PUBLISHED) {
            throw new BusinessRuleException("Нельзя изменять опубликованные записи");
        }
        if (!record.getDomain().getId().equals(request.domainId())) {
            MasterDataDomain domain = domainRepository.findById(request.domainId())
                    .orElseThrow(() -> new NotFoundException("Domain %d not found".formatted(request.domainId())));
            record.setDomain(domain);
        }
        validatePayload(request.payloadJson());
        record.setBusinessKey(request.businessKey());
        record.setPayloadJson(request.payloadJson());
        record.setSourceSystem(request.sourceSystem());
        record.setOwner(request.owner());
        record.setVersionNumber(record.getVersionNumber() + 1);
        auditService.log(record, AuditEntry.ActionType.UPDATE, request.owner(), "Обновлена запись мастер-данных");
        return MasterDataMapper.toDto(record);
    }

    @Transactional
    public MasterDataRecordDto changeStatus(Long id, RecordStatusChangeRequest request) {
        MasterDataRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(id)));
        RecordStatus newStatus = request.status();
        if (record.getStatus() == newStatus) {
            return MasterDataMapper.toDto(record);
        }
        if (!isTransitionAllowed(record.getStatus(), newStatus)) {
            throw new BusinessRuleException("Переход статуса %s -> %s запрещен"
                    .formatted(record.getStatus(), newStatus));
        }
        record.setStatus(newStatus);
        if (newStatus == RecordStatus.PUBLISHED) {
            record.setPublishedAt(java.time.Instant.now());
        }
        auditService.log(record, AuditEntry.ActionType.STATUS_CHANGE, request.actor(),
                "Статус изменен на %s. %s".formatted(newStatus, request.comment()));
        return MasterDataMapper.toDto(record);
    }

    private boolean isTransitionAllowed(RecordStatus current, RecordStatus target) {
        return switch (current) {
            case DRAFT -> target == RecordStatus.IN_REVIEW || target == RecordStatus.REJECTED;
            case IN_REVIEW -> target == RecordStatus.APPROVED || target == RecordStatus.REJECTED;
            case APPROVED -> target == RecordStatus.PUBLISHED || target == RecordStatus.REJECTED;
            case REJECTED -> target == RecordStatus.DRAFT;
            case PUBLISHED -> false;
        };
    }

    private void validatePayload(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            if (!node.isObject()) {
                throw new BusinessRuleException("Payload must be JSON object");
            }
        } catch (Exception ex) {
            throw new BusinessRuleException("Некорректный JSON payload: " + ex.getMessage());
        }
    }
}

