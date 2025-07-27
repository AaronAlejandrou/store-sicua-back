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
            
            logger.info("Store configuration retrieved successfully");
            
            return mapToResponse(storeConfig);
            
        } catch (Exception e) {
            logger.error("Error retrieving store configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve store configuration: " + e.getMessage(), e);
        }
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
