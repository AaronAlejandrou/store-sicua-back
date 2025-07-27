package com.sicua.domain.storeconfig.repository;

import com.sicua.domain.storeconfig.entity.StoreConfig;

import java.util.Optional;

/**
 * Repository interface for StoreConfig domain entity
 */
public interface StoreConfigRepository {
    
    /**
     * Find the current store configuration
     * @return Optional containing the store config if exists
     */
    Optional<StoreConfig> findCurrent();
    
    /**
     * Find store configuration by ID
     * @param id the ID to search for
     * @return Optional containing the store config if exists
     */
    Optional<StoreConfig> findById(String id);
    
    /**
     * Find store configuration by email
     * @param email the email to search for
     * @return Optional containing the store config if exists
     */
    Optional<StoreConfig> findByEmail(String email);
    
    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Save store configuration
     * @param storeConfig the store config to save
     * @return the saved store config
     */
    StoreConfig save(StoreConfig storeConfig);
}
