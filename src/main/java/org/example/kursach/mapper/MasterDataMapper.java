package org.example.kursach.mapper;

import org.example.kursach.domain.*;
import org.example.kursach.dto.*;

public class MasterDataMapper {

    private MasterDataMapper() {
    }

    public static MasterDataDomainDto toDto(MasterDataDomain domain) {
        return new MasterDataDomainDto(
                domain.getId(),
                domain.getCode(),
                domain.getName(),
                domain.getDescription(),
                domain.getSteward(),
                domain.isActive(),
                domain.getQualityScore(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public static MasterDataRecordDto toDto(MasterDataRecord record) {
        return new MasterDataRecordDto(
                record.getId(),
                record.getDomain().getId(),
                record.getDomain().getCode(),
                record.getBusinessKey(),
                record.getPayloadJson(),
                record.getStatus(),
                record.getSourceSystem(),
                record.getOwner(),
                record.getVersionNumber(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                record.getPublishedAt()
        );
    }

    public static DataQualityRuleDto toDto(DataQualityRule rule) {
        return new DataQualityRuleDto(
                rule.getId(),
                rule.getDomain().getId(),
                rule.getName(),
                rule.getDescription(),
                rule.getExpression(),
                rule.getSeverity(),
                rule.isBlocking(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }

    public static DataQualityIssueDto toDto(DataQualityIssue issue) {
        return new DataQualityIssueDto(
                issue.getId(),
                issue.getRecord().getId(),
                issue.getRuleName(),
                issue.getSeverity(),
                issue.getStatus(),
                issue.getDetails(),
                issue.getResolutionComment(),
                issue.getDetectedAt(),
                issue.getResolvedAt()
        );
    }

    public static WorkflowTaskDto toDto(WorkflowTask task) {
        return new WorkflowTaskDto(
                task.getId(),
                task.getRecord().getId(),
                task.getType(),
                task.getState(),
                task.getAssignee(),
                task.getDueDate(),
                task.getComment(),
                task.getCreatedAt(),
                task.getCompletedAt()
        );
    }

    public static IntegrationChannelDto toDto(IntegrationChannel channel) {
        return new IntegrationChannelDto(
                channel.getId(),
                channel.getCode(),
                channel.getSystemName(),
                channel.getEndpointUrl(),
                channel.getDeliveryType(),
                channel.isActive(),
                channel.getLastPublishedAt(),
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }

    public static PublicationEventDto toDto(PublicationEvent event) {
        return new PublicationEventDto(
                event.getId(),
                event.getRecord().getId(),
                event.getChannel().getId(),
                event.getStatus(),
                event.getMessage(),
                event.getCreatedAt(),
                event.getFinishedAt()
        );
    }

    public static AuditEntryDto toDto(AuditEntry entry) {
        return new AuditEntryDto(
                entry.getId(),
                entry.getRecord() != null ? entry.getRecord().getId() : null,
                entry.getActionType(),
                entry.getActor(),
                entry.getMessage(),
                entry.getCreatedAt()
        );
    }

    public static SystemUserDto toDto(SystemUser user) {
        return new SystemUserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRoles(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

