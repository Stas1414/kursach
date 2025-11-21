package org.example.kursach.web;

import jakarta.validation.Valid;
import org.example.kursach.dto.PublicationEventDto;
import org.example.kursach.dto.PublicationRequest;
import org.example.kursach.service.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {

    private final PublicationService service;

    public PublicationController(PublicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationEventDto publish(@Valid @RequestBody PublicationRequest request) {
        return service.publish(request);
    }

    @GetMapping("/{recordId}")
    public List<PublicationEventDto> history(@PathVariable Long recordId) {
        return service.history(recordId);
    }
}

