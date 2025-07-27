package com.sicua.infrastructure.persistence.storeconfig;

import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.repository.StoreConfigRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StoreConfigRepositoryImpl implements StoreConfigRepository {
    
    private final StoreConfigJpaRepository jpaRepository;
    
    public StoreConfigRepositoryImpl(StoreConfigJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<StoreConfig> findCurrent() {
        return jpaRepository.findFirst()
                .map(this::toDomain);
    }
    
    @Override
    public Optional<StoreConfig> findById(String id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<StoreConfig> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(this::toDomain);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public StoreConfig save(StoreConfig storeConfig) {
        StoreConfigEntity entity = toEntity(storeConfig);
        StoreConfigEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    private StoreConfig toDomain(StoreConfigEntity entity) {
        StoreConfig config = new StoreConfig(
                entity.getName(),
                entity.getAddress(),
                entity.getEmail(),
                entity.getPhone()
        );
        // Set the ID and password manually since constructor doesn't include them
        try {
            java.lang.reflect.Field idField = StoreConfig.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(config, entity.getId());
            
            java.lang.reflect.Field passwordField = StoreConfig.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(config, entity.getPassword());
            
            java.lang.reflect.Field createdAtField = StoreConfig.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(config, entity.getCreatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error mapping entity to domain", e);
        }
        return config;
    }
    
    private StoreConfigEntity toEntity(StoreConfig storeConfig) {
        return new StoreConfigEntity(
                storeConfig.getId(),
                storeConfig.getName(),
                storeConfig.getAddress(),
                storeConfig.getEmail(),
                storeConfig.getPhone(),
                storeConfig.getPassword(),
                storeConfig.getCreatedAt(),
                storeConfig.getUpdatedAt()
        );
    }
}
