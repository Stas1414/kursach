package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.kursach.dto.DashboardSummaryDto;
import org.example.kursach.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Сводная статистика по мастер‑данным и качеству данных.")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    @Operation(summary = "Сводка по системе", description = "Возвращает агрегированную статистику: количество записей, статусов, открытых инцидентов и распределение по доменам.")
    public DashboardSummaryDto summary() {
        return service.summary();
    }
}

