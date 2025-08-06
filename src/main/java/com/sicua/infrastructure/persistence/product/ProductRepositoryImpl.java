package com.sicua.infrastructure.persistence.product;

import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    
    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<Product> findByIdAndStoreId(ProductId productId, String storeId) {
        return jpaRepository.findByProductIdAndStoreId(productId.getValue(), storeId)
                .map(this::toDomain);
    }
    
    @Override
    public List<Product> findAllByStoreId(String storeId) {
        return jpaRepository.findByStoreId(storeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByIdAndStoreId(ProductId productId, String storeId) {
        return jpaRepository.existsByProductIdAndStoreId(productId.getValue(), storeId);
    }
    
    // Legacy method for backward compatibility - checks across all stores
    public boolean existsById(ProductId productId) {
        return jpaRepository.existsById(productId.getValue());
    }
    
    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public void deleteByIdAndStoreId(ProductId productId, String storeId) {
        jpaRepository.deleteByProductIdAndStoreId(productId.getValue(), storeId);
    }
    
    private Product toDomain(ProductEntity entity) {
        Product product = new Product(
                ProductId.of(entity.getProductId()),
                entity.getStoreId(),
                entity.getName(),
                entity.getBrand(),
                entity.getCategoryNumber(),
                entity.getSize(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
        return product;
    }
    
    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getProductId().getValue(),
                product.getStoreId(),
                product.getName(),
                product.getBrand(),
                product.getCategoryNumber(),
                product.getSize(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
