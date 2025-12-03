package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.IntegrationChannelDto;
import org.example.kursach.dto.IntegrationChannelRequest;
import org.example.kursach.service.IntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integration/channels")
@Tag(name = "Integration Channels", description = "Управление каналами публикации мастер‑данных во внешние системы.")
public class IntegrationController {

    private final IntegrationService service;

    public IntegrationController(IntegrationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Список каналов интеграции", description = "Возвращает все зарегистрированные интеграционные каналы.")
    public List<IntegrationChannelDto> list() {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать канал", description = "Добавляет новый канал интеграции (ERP, CRM и т.п.).")
    public IntegrationChannelDto create(@Valid @RequestBody IntegrationChannelRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить канал", description = "Изменяет параметры существующего канала интеграции.")
    public IntegrationChannelDto update(@PathVariable Long id,
                                        @Valid @RequestBody IntegrationChannelRequest request) {
        return service.update(id, request);
    }
}

