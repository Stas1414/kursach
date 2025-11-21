package org.example.kursach.web;

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
public class DataQualityController {

    private final DataQualityService service;

    public DataQualityController(DataQualityService service) {
        this.service = service;
    }

    @GetMapping("/rules/{domainId}")
    public List<DataQualityRuleDto> rules(@PathVariable Long domainId) {
        return service.rules(domainId);
    }

    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    public DataQualityRuleDto createRule(@Valid @RequestBody DataQualityRuleRequest request) {
        return service.createRule(request);
    }

    @PostMapping("/run/{recordId}")
    public List<DataQualityIssueDto> runChecks(@PathVariable Long recordId,
                                               @RequestParam(defaultValue = "system") String actor) {
        return service.runChecks(recordId, actor);
    }

    @PostMapping("/issues/{issueId}/resolve")
    public DataQualityIssueDto resolve(@PathVariable Long issueId, @RequestParam String comment) {
        return service.resolveIssue(issueId, comment);
    }
}

