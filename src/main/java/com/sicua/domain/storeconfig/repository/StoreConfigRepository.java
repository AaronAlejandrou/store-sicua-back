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
     * Save store configuration
     * @param storeConfig the store config to save
     * @return the saved store config
     */
    StoreConfig save(StoreConfig storeConfig);
}
