package com.sicua.application.storeconfig.usecase;

import com.sicua.application.storeconfig.dto.StoreConfigResponse;
import com.sicua.application.storeconfig.dto.UpdateStoreConfigRequest;
import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.repository.StoreConfigRepository;
import com.sicua.domain.storeconfig.service.StoreConfigDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for updating store configuration
 */
@Service
public class UpdateStoreConfigUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(UpdateStoreConfigUseCase.class);
    
    private final StoreConfigRepository storeConfigRepository;
    private final StoreConfigDomainService storeConfigDomainService;
    
    public UpdateStoreConfigUseCase(StoreConfigRepository storeConfigRepository, 
                                   StoreConfigDomainService storeConfigDomainService) {
        this.storeConfigRepository = storeConfigRepository;
        this.storeConfigDomainService = storeConfigDomainService;
    }
    
    @Transactional
    public StoreConfigResponse execute(UpdateStoreConfigRequest request) {
        logger.info("Updating store configuration");
        
        try {
            if (!storeConfigDomainService.validateStoreConfig(
                    request.getName(), request.getAddress(), request.getEmail(), request.getPhone())) {
                throw new IllegalArgumentException("Invalid store configuration data");
            }
            
            StoreConfig storeConfig = storeConfigDomainService.ensureStoreConfigExists();
            
            storeConfig.updateConfig(
                    request.getName(),
                    request.getAddress(),
                    request.getEmail(),
                    request.getPhone()
            );
            
            StoreConfig updatedConfig = storeConfigRepository.save(storeConfig);
            
            logger.info("Store configuration updated successfully");
            
            return mapToResponse(updatedConfig);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid store configuration: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating store configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update store configuration: " + e.getMessage(), e);
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
