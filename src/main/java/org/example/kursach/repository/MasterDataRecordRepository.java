package org.example.kursach.repository;

import org.example.kursach.domain.MasterDataRecord;
import org.example.kursach.domain.RecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MasterDataRecordRepository extends JpaRepository<MasterDataRecord, Long> {

    Page<MasterDataRecord> findByStatus(RecordStatus status, Pageable pageable);

    long countByStatus(RecordStatus status);

    long countByDomainId(Long domainId);

    @Query("""
            select r from MasterDataRecord r 
            where (:domainId is null or r.domain.id = :domainId)
              and (:status is null or r.status = :status)
              and (:owner is null or r.owner = :owner)
            """)
    Page<MasterDataRecord> search(
            @Param("domainId") Long domainId,
            @Param("status") RecordStatus status,
            @Param("owner") String owner,
            Pageable pageable
    );

    List<MasterDataRecord> findTop10ByStatusOrderByUpdatedAtDesc(RecordStatus status);
}

