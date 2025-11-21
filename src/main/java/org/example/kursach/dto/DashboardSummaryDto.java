package org.example.kursach.dto;

import java.util.Map;

public record DashboardSummaryDto(
        long totalRecords,
        long draftRecords,
        long inReviewRecords,
        long approvedRecords,
        long publishedRecords,
        long openIssues,
        Map<String, Long> domainDistribution
) {}

