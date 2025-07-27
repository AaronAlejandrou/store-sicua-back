package com.sicua.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    
    List<ProductEntity> findByStoreId(String storeId);
    
    Optional<ProductEntity> findByProductIdAndStoreId(String productId, String storeId);
    
    boolean existsByProductIdAndStoreId(String productId, String storeId);
    
    void deleteByProductIdAndStoreId(String productId, String storeId);
}
