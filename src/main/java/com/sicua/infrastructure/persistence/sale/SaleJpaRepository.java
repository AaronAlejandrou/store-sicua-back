package com.sicua.infrastructure.persistence.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleJpaRepository extends JpaRepository<SaleEntity, String> {
    
    @Query("SELECT s FROM SaleEntity s ORDER BY s.createdAt DESC")
    List<SaleEntity> findAllOrderByCreatedAtDesc();
    
    List<SaleEntity> findByInvoiced(Boolean invoiced);
}
