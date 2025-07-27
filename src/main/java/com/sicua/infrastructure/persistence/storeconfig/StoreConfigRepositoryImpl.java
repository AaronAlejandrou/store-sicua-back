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
        return config;
    }
    
    private StoreConfigEntity toEntity(StoreConfig storeConfig) {
        return new StoreConfigEntity(
                storeConfig.getId(),
                storeConfig.getName(),
                storeConfig.getAddress(),
                storeConfig.getEmail(),
                storeConfig.getPhone(),
                storeConfig.getUpdatedAt()
        );
    }
}
