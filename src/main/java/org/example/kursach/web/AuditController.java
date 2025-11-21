package org.example.kursach.web;

import org.example.kursach.dto.AuditEntryDto;
import org.example.kursach.service.AuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping("/latest")
    public List<AuditEntryDto> latest() {
        return service.latest();
    }

    @GetMapping("/record/{recordId}")
    public List<AuditEntryDto> record(@PathVariable Long recordId) {
        return service.byRecord(recordId);
    }
}

