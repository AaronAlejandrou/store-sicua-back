package com.sicua.domain.storeconfig.service;

import com.sicua.application.auth.SessionService;
import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.repository.StoreConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Domain service for StoreConfig business logic
 */
@Service
public class StoreConfigDomainService {
    
    private static final Logger logger = LoggerFactory.getLogger(StoreConfigDomainService.class);
    
    private final StoreConfigRepository storeConfigRepository;
    private final SessionService sessionService;
    
    public StoreConfigDomainService(StoreConfigRepository storeConfigRepository, SessionService sessionService) {
        this.storeConfigRepository = storeConfigRepository;
        this.sessionService = sessionService;
    }
    
    /**
     * Ensures there's always a store configuration available for the current user
     * @return the current user's store configuration
     */
    public StoreConfig ensureStoreConfigExists() {
        logger.info("ensureStoreConfigExists() called");
        
        try {
            String storeId = sessionService.getCurrentStoreId();
            logger.info("Current store ID from session: {}", storeId);
            
            return storeConfigRepository.findById(storeId)
                    .orElseThrow(() -> {
                        logger.error("Store configuration not found for storeId: {}", storeId);
                        return new IllegalStateException("Store configuration not found for current user. StoreId: " + storeId);
                    });
        } catch (Exception e) {
            logger.error("Error in ensureStoreConfigExists(): {}", e.getMessage(), e);
            throw e;
        }
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
