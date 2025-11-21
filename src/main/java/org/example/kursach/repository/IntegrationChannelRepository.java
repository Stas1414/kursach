package org.example.kursach.repository;

import org.example.kursach.domain.IntegrationChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegrationChannelRepository extends JpaRepository<IntegrationChannel, Long> {
    Optional<IntegrationChannel> findByCode(String code);
}

