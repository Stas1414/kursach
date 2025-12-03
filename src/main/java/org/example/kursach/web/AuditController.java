package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.kursach.dto.AuditEntryDto;
import org.example.kursach.service.AuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "Аудит действий пользователей и системы.")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping("/latest")
    @Operation(summary = "Последние события аудита", description = "Возвращает последние 20 записей журнала аудита.")
    public List<AuditEntryDto> latest() {
        return service.latest();
    }

    @GetMapping("/record/{recordId}")
    @Operation(summary = "Аудит по мастер‑записи", description = "Возвращает историю изменений по конкретной мастер‑записи.")
    public List<AuditEntryDto> record(@PathVariable Long recordId) {
        return service.byRecord(recordId);
    }
}

