package org.example.kursach.repository;

import org.example.kursach.domain.DataQualityRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataQualityRuleRepository extends JpaRepository<DataQualityRule, Long> {
    List<DataQualityRule> findByDomainId(Long domainId);
}

