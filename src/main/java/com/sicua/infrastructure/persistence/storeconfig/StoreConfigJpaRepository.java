package com.sicua.infrastructure.persistence.storeconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreConfigJpaRepository extends JpaRepository<StoreConfigEntity, String> {
    
    @Query("SELECT s FROM StoreConfigEntity s ORDER BY s.updatedAt DESC LIMIT 1")
    Optional<StoreConfigEntity> findFirst();
    
    Optional<StoreConfigEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
