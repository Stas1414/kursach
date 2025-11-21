package org.example.kursach.dto;

import org.example.kursach.domain.RecordStatus;

public record RecordSearchRequest(
        Long domainId,
        RecordStatus status,
        String owner,
        int page,
        int size
) {
    public RecordSearchRequest {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0 || size > 100) {
            size = 20;
        }
    }
}

