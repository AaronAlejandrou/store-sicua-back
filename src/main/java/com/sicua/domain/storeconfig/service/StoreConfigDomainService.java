package com.sicua.domain.storeconfig.service;

import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.repository.StoreConfigRepository;
import org.springframework.stereotype.Service;

/**
 * Domain service for StoreConfig business logic
 */
@Service
public class StoreConfigDomainService {
    
    private final StoreConfigRepository storeConfigRepository;
    
    public StoreConfigDomainService(StoreConfigRepository storeConfigRepository) {
        this.storeConfigRepository = storeConfigRepository;
    }
    
    /**
     * Ensures there's always a store configuration available
     * @return the current or default store configuration
     */
    public StoreConfig ensureStoreConfigExists() {
        return storeConfigRepository.findCurrent()
                .orElseGet(() -> {
                    StoreConfig defaultConfig = new StoreConfig(
                            "SICUA Store",
                            "Default Address",
                            "contact@sicua.com",
                            "000-000-0000"
                    );
                    return storeConfigRepository.save(defaultConfig);
                });
    }
    
    /**
     * Validates store configuration data
     * @param name store name
     * @param address store address
     * @param email store email
     * @param phone store phone
     * @return true if all required fields are present
     */
    public boolean validateStoreConfig(String name, String address, String email, String phone) {
        return name != null && !name.trim().isEmpty();
    }
}
