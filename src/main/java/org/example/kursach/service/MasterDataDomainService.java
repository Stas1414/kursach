package org.example.kursach.service;

import org.example.kursach.domain.MasterDataDomain;
import org.example.kursach.dto.MasterDataDomainDto;
import org.example.kursach.dto.MasterDataDomainRequest;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MasterDataDomainService {

    private final MasterDataDomainRepository repository;

    public MasterDataDomainService(MasterDataDomainRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MasterDataDomainDto> findAll() {
        return repository.findAll().stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MasterDataDomainDto findById(Long id) {
        return repository.findById(id)
                .map(MasterDataMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Domain %d not found".formatted(id)));
    }

    @Transactional
    public MasterDataDomainDto create(MasterDataDomainRequest request) {
        if (repository.existsByCode(request.code())) {
            throw new BusinessRuleException("Domain code already exists");
        }
        MasterDataDomain domain = new MasterDataDomain();
        updateEntity(domain, request);
        return MasterDataMapper.toDto(repository.save(domain));
    }

    @Transactional
    public MasterDataDomainDto update(Long id, MasterDataDomainRequest request) {
        MasterDataDomain domain = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domain %d not found".formatted(id)));
        if (!domain.getCode().equals(request.code()) && repository.existsByCode(request.code())) {
            throw new BusinessRuleException("Domain code already exists");
        }
        updateEntity(domain, request);
        return MasterDataMapper.toDto(domain);
    }

    private void updateEntity(MasterDataDomain domain, MasterDataDomainRequest request) {
        domain.setCode(request.code());
        domain.setName(request.name());
        domain.setDescription(request.description());
        domain.setSteward(request.steward());
        domain.setActive(request.active());
        domain.setQualityScore(request.qualityScore());
    }
}

