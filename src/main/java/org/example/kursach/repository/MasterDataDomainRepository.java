package org.example.kursach.repository;

import org.example.kursach.domain.MasterDataDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterDataDomainRepository extends JpaRepository<MasterDataDomain, Long> {
    Optional<MasterDataDomain> findByCode(String code);
    boolean existsByCode(String code);
}

