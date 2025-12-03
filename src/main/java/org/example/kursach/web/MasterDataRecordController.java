package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.*;
import org.example.kursach.service.MasterDataRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@Tag(name = "Master Records", description = "CRUD‑операции над мастер‑записями и управление их статусами.")
public class MasterDataRecordController {

    private final MasterDataRecordService service;

    public MasterDataRecordController(MasterDataRecordService service) {
        this.service = service;
    }

    @PostMapping("/search")
    @Operation(summary = "Поиск мастер‑записей", description = "Постраничный поиск записей по домену, статусу и владельцу.")
    public PageResponse<MasterDataRecordDto> search(@RequestBody RecordSearchRequest request) {
        return service.search(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить мастер‑запись", description = "Возвращает мастер‑запись по идентификатору.")
    public MasterDataRecordDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать мастер‑запись", description = "Создаёт новую мастер‑запись в выбранном домене.")
    public MasterDataRecordDto create(@Valid @RequestBody MasterDataRecordRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить мастер‑запись", description = "Изменяет существующую мастер‑запись.")
    public MasterDataRecordDto update(@PathVariable Long id, @Valid @RequestBody MasterDataRecordRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "Сменить статус мастер‑записи", description = "Переводит запись между статусами DRAFT/IN_REVIEW/APPROVED/PUBLISHED.")
    public MasterDataRecordDto changeStatus(@PathVariable Long id,
                                            @Valid @RequestBody RecordStatusChangeRequest request) {
        return service.changeStatus(id, request);
    }
}

