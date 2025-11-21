package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MasterDataRecordRequest(
        @NotNull Long domainId,
        @NotBlank @Size(max = 128) String businessKey,
        @NotBlank String payloadJson,
        @Size(max = 120) String sourceSystem,
        @Size(max = 120) String owner
) {}

