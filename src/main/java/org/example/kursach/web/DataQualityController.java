package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.DataQualityIssueDto;
import org.example.kursach.dto.DataQualityRuleDto;
import org.example.kursach.dto.DataQualityRuleRequest;
import org.example.kursach.service.DataQualityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality")
@Tag(name = "Data Quality", description = "Правила и проверки качества мастер‑данных.")
public class DataQualityController {

    private final DataQualityService service;

    public DataQualityController(DataQualityService service) {
        this.service = service;
    }

    @GetMapping("/rules/{domainId}")
    @Operation(summary = "Список правил качества", description = "Возвращает правила качества для указанного домена.")
    public List<DataQualityRuleDto> rules(@PathVariable Long domainId) {
        return service.rules(domainId);
    }

    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать правило качества", description = "Создаёт новое правило проверки качества данных.")
    public DataQualityRuleDto createRule(@Valid @RequestBody DataQualityRuleRequest request) {
        return service.createRule(request);
    }

    @PostMapping("/run/{recordId}")
    @Operation(summary = "Запустить проверку качества", description = "Выполняет все правила качества для указанной мастер‑записи.")
    public List<DataQualityIssueDto> runChecks(@PathVariable Long recordId,
                                               @RequestParam(defaultValue = "system") String actor) {
        return service.runChecks(recordId, actor);
    }

    @PostMapping("/issues/{issueId}/resolve")
    @Operation(summary = "Закрыть инцидент качества", description = "Помечает инцидент качества как решённый.")
    public DataQualityIssueDto resolve(@PathVariable Long issueId, @RequestParam String comment) {
        return service.resolveIssue(issueId, comment);
    }
}

