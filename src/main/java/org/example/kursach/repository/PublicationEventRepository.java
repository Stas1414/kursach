package org.example.kursach.repository;

import org.example.kursach.domain.PublicationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationEventRepository extends JpaRepository<PublicationEvent, Long> {
    List<PublicationEvent> findTop5ByRecordIdOrderByCreatedAtDesc(Long recordId);
}

