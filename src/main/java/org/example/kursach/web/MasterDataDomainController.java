package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.MasterDataDomainDto;
import org.example.kursach.dto.MasterDataDomainRequest;
import org.example.kursach.service.MasterDataDomainService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
@Tag(name = "Domains", description = "Управление доменами мастер‑данных (справочниками верхнего уровня).")
public class MasterDataDomainController {

    private final MasterDataDomainService service;

    public MasterDataDomainController(MasterDataDomainService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Список доменов", description = "Возвращает все домены мастер‑данных.")
    public List<MasterDataDomainDto> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить домен", description = "Возвращает домен по идентификатору.")
    public MasterDataDomainDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать домен", description = "Создаёт новый домен мастер‑данных.")
    public MasterDataDomainDto create(@Valid @RequestBody MasterDataDomainRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить домен", description = "Изменяет атрибуты существующего домена.")
    public MasterDataDomainDto update(@PathVariable Long id, @Valid @RequestBody MasterDataDomainRequest request) {
        return service.update(id, request);
    }
}

