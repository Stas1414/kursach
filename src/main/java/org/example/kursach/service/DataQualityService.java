package org.example.kursach.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kursach.domain.*;
import org.example.kursach.dto.DataQualityIssueDto;
import org.example.kursach.dto.DataQualityRuleDto;
import org.example.kursach.dto.DataQualityRuleRequest;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.DataQualityIssueRepository;
import org.example.kursach.repository.DataQualityRuleRepository;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataQualityService {

    private final DataQualityRuleRepository ruleRepository;
    private final DataQualityIssueRepository issueRepository;
    private final MasterDataDomainRepository domainRepository;
    private final MasterDataRecordRepository recordRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public DataQualityService(DataQualityRuleRepository ruleRepository,
                              DataQualityIssueRepository issueRepository,
                              MasterDataDomainRepository domainRepository,
                              MasterDataRecordRepository recordRepository,
                              AuditService auditService,
                              ObjectMapper objectMapper) {
        this.ruleRepository = ruleRepository;
        this.issueRepository = issueRepository;
        this.domainRepository = domainRepository;
        this.recordRepository = recordRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<DataQualityRuleDto> rules(Long domainId) {
        return ruleRepository.findByDomainId(domainId).stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional
    public DataQualityRuleDto createRule(DataQualityRuleRequest request) {
        MasterDataDomain domain = domainRepository.findById(request.domainId())
                .orElseThrow(() -> new NotFoundException("Domain %d not found".formatted(request.domainId())));
        DataQualityRule rule = new DataQualityRule();
        rule.setDomain(domain);
        rule.setName(request.name());
        rule.setDescription(request.description());
        rule.setExpression(request.expression());
        rule.setSeverity(request.severity());
        rule.setBlocking(request.blocking());
        return MasterDataMapper.toDto(ruleRepository.save(rule));
    }

    @Transactional
    public List<DataQualityIssueDto> runChecks(Long recordId, String actor) {
        MasterDataRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Record %d not found".formatted(recordId)));
        List<DataQualityRule> rules = ruleRepository.findByDomainId(record.getDomain().getId());
        List<DataQualityIssue> issues = new ArrayList<>();
        JsonNode payload;
        try {
            payload = objectMapper.readTree(record.getPayloadJson());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid payload JSON", e);
        }
        issueRepository.deleteAll(issueRepository.findByRecordId(recordId));
        for (DataQualityRule rule : rules) {
            if (!evaluate(payload, rule.getExpression())) {
                DataQualityIssue issue = new DataQualityIssue();
                issue.setRecord(record);
                issue.setRuleName(rule.getName());
                issue.setSeverity(rule.getSeverity());
                issue.setDetails("Выражение %s не выполнено".formatted(rule.getExpression()));
                issue.setStatus(rule.isBlocking() ? IssueStatus.OPEN : IssueStatus.IN_PROGRESS);
                issues.add(issueRepository.save(issue));
            }
        }
        auditService.log(record, AuditEntry.ActionType.QUALITY_CHECK, actor,
                "Запущена проверка качества данных. Найдено %d проблем".formatted(issues.size()));
        return issues.stream().map(MasterDataMapper::toDto).toList();
    }

    @Transactional
    public DataQualityIssueDto resolveIssue(Long issueId, String comment) {
        DataQualityIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Issue %d not found".formatted(issueId)));
        issue.setStatus(IssueStatus.RESOLVED);
        issue.setResolutionComment(comment);
        return MasterDataMapper.toDto(issue);
    }

    private boolean evaluate(JsonNode payload, String expression) {
        // Interpret expression as JSON Pointer that must exist and not be null/blank
        JsonNode node = payload.at(expression);
        if (node.isMissingNode() || node.isNull()) {
            return false;
        }
        return !node.isTextual() || !node.asText().isBlank();
    }
}

