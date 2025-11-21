package org.example.kursach.service;

import org.example.kursach.domain.IssueStatus;
import org.example.kursach.domain.MasterDataDomain;
import org.example.kursach.domain.RecordStatus;
import org.example.kursach.dto.DashboardSummaryDto;
import org.example.kursach.repository.DataQualityIssueRepository;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final MasterDataRecordRepository recordRepository;
    private final MasterDataDomainRepository domainRepository;
    private final DataQualityIssueRepository issueRepository;

    public DashboardService(MasterDataRecordRepository recordRepository,
                            MasterDataDomainRepository domainRepository,
                            DataQualityIssueRepository issueRepository) {
        this.recordRepository = recordRepository;
        this.domainRepository = domainRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto summary() {
        long total = recordRepository.count();
        Map<RecordStatus, Long> byStatus = new EnumMap<>(RecordStatus.class);
        for (RecordStatus status : RecordStatus.values()) {
            byStatus.put(status, recordRepository.countByStatus(status));
        }
        Map<String, Long> domainDistribution = domainRepository.findAll().stream()
                .collect(Collectors.toMap(
                        MasterDataDomain::getCode,
                        domain -> recordRepository.countByDomainId(domain.getId()),
                        (a, b) -> a));
        return new DashboardSummaryDto(
                total,
                byStatus.getOrDefault(RecordStatus.DRAFT, 0L),
                byStatus.getOrDefault(RecordStatus.IN_REVIEW, 0L),
                byStatus.getOrDefault(RecordStatus.APPROVED, 0L),
                byStatus.getOrDefault(RecordStatus.PUBLISHED, 0L),
                issueRepository.countByStatus(IssueStatus.OPEN),
                domainDistribution
        );
    }
}

