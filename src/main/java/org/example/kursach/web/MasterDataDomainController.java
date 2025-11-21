package org.example.kursach.web;

import jakarta.validation.Valid;
import org.example.kursach.dto.MasterDataDomainDto;
import org.example.kursach.dto.MasterDataDomainRequest;
import org.example.kursach.service.MasterDataDomainService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
public class MasterDataDomainController {

    private final MasterDataDomainService service;

    public MasterDataDomainController(MasterDataDomainService service) {
        this.service = service;
    }

    @GetMapping
    public List<MasterDataDomainDto> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MasterDataDomainDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MasterDataDomainDto create(@Valid @RequestBody MasterDataDomainRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MasterDataDomainDto update(@PathVariable Long id, @Valid @RequestBody MasterDataDomainRequest request) {
        return service.update(id, request);
    }
}

