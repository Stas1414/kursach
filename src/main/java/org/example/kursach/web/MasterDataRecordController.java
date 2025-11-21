package org.example.kursach.web;

import jakarta.validation.Valid;
import org.example.kursach.dto.*;
import org.example.kursach.service.MasterDataRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
public class MasterDataRecordController {

    private final MasterDataRecordService service;

    public MasterDataRecordController(MasterDataRecordService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public PageResponse<MasterDataRecordDto> search(@RequestBody RecordSearchRequest request) {
        return service.search(request);
    }

    @GetMapping("/{id}")
    public MasterDataRecordDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MasterDataRecordDto create(@Valid @RequestBody MasterDataRecordRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MasterDataRecordDto update(@PathVariable Long id, @Valid @RequestBody MasterDataRecordRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/status")
    public MasterDataRecordDto changeStatus(@PathVariable Long id,
                                            @Valid @RequestBody RecordStatusChangeRequest request) {
        return service.changeStatus(id, request);
    }
}

