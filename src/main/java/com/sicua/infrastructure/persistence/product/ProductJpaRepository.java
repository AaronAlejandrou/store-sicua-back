package com.sicua.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    
    @Query("SELECT p FROM ProductEntity p WHERE p.storeId = :storeId ORDER BY p.createdAt DESC")
    List<ProductEntity> findByStoreId(@Param("storeId") String storeId);
    
    Optional<ProductEntity> findByProductIdAndStoreId(String productId, String storeId);
    
    boolean existsByProductIdAndStoreId(String productId, String storeId);
    
    void deleteByProductIdAndStoreId(String productId, String storeId);
}
