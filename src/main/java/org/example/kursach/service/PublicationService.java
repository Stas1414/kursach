package org.example.kursach.service;

import org.example.kursach.domain.*;
import org.example.kursach.dto.PublicationEventDto;
import org.example.kursach.dto.PublicationRequest;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.IntegrationChannelRepository;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.example.kursach.repository.PublicationEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PublicationService {

    private final PublicationEventRepository eventRepository;
    private final MasterDataRecordRepository recordRepository;
    private final IntegrationChannelRepository channelRepository;
    private final AuditService auditService;

    public PublicationService(PublicationEventRepository eventRepository,
                              MasterDataRecordRepository recordRepository,
                              IntegrationChannelRepository channelRepository,
                              AuditService auditService) {
        this.eventRepository = eventRepository;
        this.recordRepository = recordRepository;
        this.channelRepository = channelRepository;
        this.auditService = auditService;
    }

    @Transactional
    public PublicationEventDto publish(PublicationRequest request) {
        MasterDataRecord record = recordRepository.findById(request.recordId())
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(request.recordId())));
        if (record.getStatus() != RecordStatus.APPROVED && record.getStatus() != RecordStatus.PUBLISHED) {
            throw new BusinessRuleException("Допускается публикация только утвержденных записей");
        }
        IntegrationChannel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NotFoundException("Channel %d not found".formatted(request.channelId())));
        PublicationEvent event = new PublicationEvent();
        event.setRecord(record);
        event.setChannel(channel);
        event.setStatus(PublicationEvent.Status.IN_PROGRESS);
        event.setMessage("Отправка в систему %s".formatted(channel.getSystemName()));
        PublicationEvent saved = eventRepository.save(event);
        channel.setLastPublishedAt(Instant.now());
        record.setStatus(RecordStatus.PUBLISHED);
        record.setPublishedAt(Instant.now());
        saved.setStatus(PublicationEvent.Status.SUCCESS);
        saved.setFinishedAt(Instant.now());
        saved.setMessage("Публикация успешна (%s)".formatted(channel.getDeliveryType()));
        auditService.log(record, AuditEntry.ActionType.PUBLICATION, request.actor(),
                "Запись опубликована в канал %s".formatted(channel.getCode()));
        return MasterDataMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PublicationEventDto> history(Long recordId) {
        return eventRepository.findTop5ByRecordIdOrderByCreatedAtDesc(recordId).stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }
}

