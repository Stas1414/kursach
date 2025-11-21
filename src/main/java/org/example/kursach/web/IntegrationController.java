package org.example.kursach.web;

import jakarta.validation.Valid;
import org.example.kursach.dto.IntegrationChannelDto;
import org.example.kursach.dto.IntegrationChannelRequest;
import org.example.kursach.service.IntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integration/channels")
public class IntegrationController {

    private final IntegrationService service;

    public IntegrationController(IntegrationService service) {
        this.service = service;
    }

    @GetMapping
    public List<IntegrationChannelDto> list() {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IntegrationChannelDto create(@Valid @RequestBody IntegrationChannelRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public IntegrationChannelDto update(@PathVariable Long id,
                                        @Valid @RequestBody IntegrationChannelRequest request) {
        return service.update(id, request);
    }
}

