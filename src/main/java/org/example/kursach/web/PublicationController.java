package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.PublicationEventDto;
import org.example.kursach.dto.PublicationRequest;
import org.example.kursach.service.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publication")
@Tag(name = "Publication", description = "Публикация мастер‑данных во внешние системы и журнал публикаций.")
public class PublicationController {

    private final PublicationService service;

    public PublicationController(PublicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Опубликовать мастер‑запись", description = "Отправляет мастер‑запись в выбранный интеграционный канал.")
    public PublicationEventDto publish(@Valid @RequestBody PublicationRequest request) {
        return service.publish(request);
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "История публикаций", description = "Возвращает последние события публикации для мастер‑записи.")
    public List<PublicationEventDto> history(@PathVariable Long recordId) {
        return service.history(recordId);
    }
}

