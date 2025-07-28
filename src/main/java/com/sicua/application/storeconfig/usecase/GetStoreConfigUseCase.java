package com.sicua.application.storeconfig.usecase;

import com.sicua.application.storeconfig.dto.StoreConfigResponse;
import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.service.StoreConfigDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for retrieving store configuration
 */
@Service
public class GetStoreConfigUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(GetStoreConfigUseCase.class);
    
    private final StoreConfigDomainService storeConfigDomainService;
    
    public GetStoreConfigUseCase(StoreConfigDomainService storeConfigDomainService) {
        this.storeConfigDomainService = storeConfigDomainService;
    }
    
    @Transactional(readOnly = true)
    public StoreConfigResponse execute() {
        logger.info("Retrieving store configuration");
        
        try {
            StoreConfig storeConfig = storeConfigDomainService.ensureStoreConfigExists();
            
            logger.info("Store configuration retrieved successfully for store: {}", storeConfig.getName());
            
            return mapToResponse(storeConfig);
            
        } catch (IllegalStateException e) {
            logger.error("Store configuration not found: {}", e.getMessage());
            // For now, return a default configuration to avoid 500 errors
            // This is a temporary fix - the real issue needs to be resolved
            StoreConfig defaultConfig = createDefaultStoreConfig();
            return mapToResponse(defaultConfig);
            
        } catch (Exception e) {
            logger.error("Error retrieving store configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve store configuration: " + e.getMessage(), e);
        }
    }
    
    private StoreConfig createDefaultStoreConfig() {
        logger.warn("Creating default store configuration as fallback");
        return new StoreConfig(
            "Mi Tienda", 
            "Dirección no configurada", 
            "email@ejemplo.com", 
            "Teléfono no configurado"
        );
    }
    
    private StoreConfigResponse mapToResponse(StoreConfig storeConfig) {
        return new StoreConfigResponse(
                storeConfig.getId(),
                storeConfig.getName(),
                storeConfig.getAddress(),
                storeConfig.getEmail(),
                storeConfig.getPhone(),
                storeConfig.getUpdatedAt()
        );
    }
}
