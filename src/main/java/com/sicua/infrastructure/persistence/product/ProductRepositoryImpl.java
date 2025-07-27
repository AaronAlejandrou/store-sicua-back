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
    public Optional<Product> findById(ProductId productId) {
        return jpaRepository.findById(productId.getValue())
                .map(this::toDomain);
    }
    
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public void deleteById(ProductId productId) {
        jpaRepository.deleteById(productId.getValue());
    }
    
    @Override
    public boolean existsById(ProductId productId) {
        return jpaRepository.existsById(productId.getValue());
    }
    
    private Product toDomain(ProductEntity entity) {
        Product product = new Product(
                ProductId.of(entity.getProductId()),
                entity.getName(),
                entity.getBrand(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getQuantity()
        );
        return product;
    }
    
    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getProductId().getValue(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
