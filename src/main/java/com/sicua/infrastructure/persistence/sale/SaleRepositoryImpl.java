package com.sicua.infrastructure.persistence.sale;

import com.sicua.domain.product.valueobject.ProductId;
import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.entity.SaleItem;
import com.sicua.domain.sale.repository.SaleRepository;
import com.sicua.domain.sale.valueobject.SaleId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SaleRepositoryImpl implements SaleRepository {
    
    private final SaleJpaRepository jpaRepository;
    
    public SaleRepositoryImpl(SaleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<Sale> findById(SaleId saleId) {
        return jpaRepository.findById(saleId.getValue())
                .map(this::toDomain);
    }
    
    @Override
    public List<Sale> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = toEntity(sale);
        
        // For updates, we need to preserve the existing entity to avoid orphaning items
        if (sale.getId() != null) {
            Optional<SaleEntity> existingEntity = jpaRepository.findById(sale.getId().getValue());
            if (existingEntity.isPresent()) {
                SaleEntity existing = existingEntity.get();
                // Update only the fields that can change
                existing.setInvoiced(sale.getInvoiced());
                // Don't touch the items collection for mark as invoiced operation
                SaleEntity savedEntity = jpaRepository.save(existing);
                return toDomain(savedEntity);
            }
        }
        
        SaleEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public List<Sale> findAllOrderByCreatedAtDesc() {
        return jpaRepository.findAllOrderByCreatedAtDesc().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sale> findByInvoiced(boolean invoiced) {
        return jpaRepository.findByInvoiced(invoiced).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Sale> findByIdAndStoreId(SaleId saleId, String storeId) {
        SaleEntity entity = jpaRepository.findByIdAndStoreId(saleId.getValue(), storeId);
        return entity != null ? Optional.of(toDomain(entity)) : Optional.empty();
    }
    
    @Override
    public List<Sale> findAllByStoreId(String storeId) {
        return jpaRepository.findByStoreId(storeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sale> findAllByStoreIdOrderByCreatedAtDesc(String storeId) {
        return jpaRepository.findByStoreIdOrderByCreatedAtDesc(storeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sale> findByInvoicedAndStoreId(boolean invoiced, String storeId) {
        return jpaRepository.findByInvoicedAndStoreId(invoiced, storeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    private Sale toDomain(SaleEntity entity) {
        List<SaleItem> items = entity.getItems().stream()
                .map(this::saleItemToDomain)
                .collect(Collectors.toList());
        
        Sale sale = new Sale(
                SaleId.of(entity.getId()),
                entity.getStoreId(),
                entity.getClientDni(),
                entity.getClientName(),
                items
        );
        
        // Set the original dates from the entity
        sale.setDate(entity.getDate());
        sale.setCreatedAt(entity.getCreatedAt());
        
        if (entity.getInvoiced()) {
            sale.markAsInvoiced();
        }
        
        return sale;
    }
    
    private SaleItem saleItemToDomain(SaleItemEntity entity) {
        return new SaleItem(
                ProductId.of(entity.getProductId()),
                entity.getName(),
                entity.getPrice(),
                entity.getQuantity()
        );
    }
    
    private SaleEntity toEntity(Sale sale) {
        SaleEntity entity = new SaleEntity(
                sale.getId().getValue(),
                sale.getStoreId(),
                sale.getClientDni(),
                sale.getClientName(),
                sale.getDate(),
                sale.getTotal(),
                sale.getInvoiced(),
                sale.getCreatedAt()
        );
        
        List<SaleItemEntity> itemEntities = sale.getItems().stream()
                .map(item -> saleItemToEntity(item, sale.getId().getValue()))
                .collect(Collectors.toList());
        
        entity.setItems(itemEntities);
        return entity;
    }
    
    private SaleItemEntity saleItemToEntity(SaleItem item, String saleId) {
        return new SaleItemEntity(
                saleId,
                item.getProductId().getValue(),
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
