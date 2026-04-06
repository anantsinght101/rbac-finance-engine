package com.zorvyn.assignment.repository;

import com.zorvyn.assignment.entity.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
    
    Page<TransactionRecord> findByDeletedFalse(Pageable pageable);

    List<TransactionRecord> findByDeletedTrue();

    // 👉 Added custom query to handle dynamic filtering + pagination
    @Query("SELECT t FROM TransactionRecord t WHERE t.deleted = false " +
           "AND (:type IS NULL OR t.type = :type) " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:startDate IS NULL OR t.date >= :startDate) " +
           "AND (:endDate IS NULL OR t.date <= :endDate)")
    Page<TransactionRecord> findFilteredAndPaged(
            @Param("type") TransactionRecord.Type type,
            @Param("category") TransactionRecord.Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}