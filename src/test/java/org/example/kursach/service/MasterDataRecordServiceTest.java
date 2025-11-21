package org.example.kursach.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kursach.domain.MasterDataDomain;
import org.example.kursach.dto.MasterDataRecordDto;
import org.example.kursach.dto.MasterDataRecordRequest;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MasterDataRecordServiceTest {

    @Autowired
    private MasterDataRecordService recordService;

    @Autowired
    private MasterDataDomainRepository domainRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MasterDataDomain domain;

    @BeforeEach
    void setUp() {
        domain = domainRepository.findByCode("TEST")
                .orElseGet(() -> {
                    MasterDataDomain d = new MasterDataDomain();
                    d.setCode("TEST");
                    d.setName("Test domain");
                    d.setSteward("qa@corp.local");
                    return domainRepository.save(d);
                });
    }

    @Test
    void shouldCreateRecord() throws Exception {
        String payload = objectMapper.writeValueAsString(java.util.Map.of("name", "Demo"));
        MasterDataRecordDto dto = recordService.create(new MasterDataRecordRequest(
                domain.getId(),
                "KEY-1",
                payload,
                "CRM",
                "qa@corp.local"
        ));
        assertThat(dto.id()).isNotNull();
        assertThat(dto.domainCode()).isEqualTo("TEST");
        assertThat(dto.owner()).isEqualTo("qa@corp.local");
    }

    @Test
    void shouldRejectInvalidPayload() {
        assertThrows(BusinessRuleException.class, () ->
                recordService.create(new MasterDataRecordRequest(
                        domain.getId(),
                        "KEY-2",
                        "not-json",
                        null,
                        "qa@corp.local"
                )));
    }
}

