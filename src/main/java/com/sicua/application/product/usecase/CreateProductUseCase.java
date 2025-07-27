package com.sicua.application.product.usecase;

import com.sicua.application.product.dto.CreateProductRequest;
import com.sicua.application.product.dto.ProductResponse;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new product
 */
@Service
public class CreateProductUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CreateProductUseCase.class);
    
    private final ProductRepository productRepository;
    
    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        logger.info("Creating new product: {} with ID: {}", request.getName(), request.getProductId());
        
        try {
            // Check if product ID already exists
            if (productRepository.existsById(ProductId.of(request.getProductId()))) {
                throw new IllegalArgumentException("Product with ID " + request.getProductId() + " already exists");
            }
            
            Product product = new Product(
                    ProductId.of(request.getProductId()),
                    request.getName(),
                    request.getBrand(),
                    request.getCategory(),
                    request.getPrice(),
                    request.getQuantity()
            );
            
            Product savedProduct = productRepository.save(product);
            
            logger.info("Product created successfully with ID: {}", savedProduct.getProductId().getValue());
            
            return mapToResponse(savedProduct);
            
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }
    
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
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
