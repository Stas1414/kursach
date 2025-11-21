package org.example.kursach.service;

import org.example.kursach.domain.IntegrationChannel;
import org.example.kursach.dto.IntegrationChannelDto;
import org.example.kursach.dto.IntegrationChannelRequest;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.IntegrationChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IntegrationService {

    private final IntegrationChannelRepository repository;

    public IntegrationService(IntegrationChannelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<IntegrationChannelDto> all() {
        return repository.findAll().stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional
    public IntegrationChannelDto create(IntegrationChannelRequest request) {
        if (repository.findByCode(request.code()).isPresent()) {
            throw new BusinessRuleException("Channel code already exists");
        }
        IntegrationChannel channel = new IntegrationChannel();
        update(channel, request);
        return MasterDataMapper.toDto(repository.save(channel));
    }

    @Transactional
    public IntegrationChannelDto update(Long id, IntegrationChannelRequest request) {
        IntegrationChannel channel = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Channel %d not found".formatted(id)));
        if (!channel.getCode().equals(request.code()) && repository.findByCode(request.code()).isPresent()) {
            throw new BusinessRuleException("Channel code already exists");
        }
        update(channel, request);
        return MasterDataMapper.toDto(channel);
    }

    private void update(IntegrationChannel channel, IntegrationChannelRequest request) {
        channel.setCode(request.code());
        channel.setSystemName(request.systemName());
        channel.setEndpointUrl(request.endpointUrl());
        if (request.deliveryType() != null) {
            channel.setDeliveryType(request.deliveryType());
        }
        channel.setActive(request.active());
    }
}

