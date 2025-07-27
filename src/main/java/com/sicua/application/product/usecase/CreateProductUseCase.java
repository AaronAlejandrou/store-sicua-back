package com.sicua.application.product.usecase;

import com.sicua.application.auth.SessionService;
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
    private final SessionService sessionService;
    
    public CreateProductUseCase(ProductRepository productRepository, SessionService sessionService) {
        this.productRepository = productRepository;
        this.sessionService = sessionService;
    }
    
    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        String storeId = sessionService.getCurrentStoreId();
        logger.info("Creating new product: {} for store: {}", request.getName(), storeId);
        
        try {
            // Check if product ID already exists for this store
            if (productRepository.existsByIdAndStoreId(ProductId.of(request.getProductId()), storeId)) {
                throw new IllegalArgumentException("Product with ID " + request.getProductId() + " already exists in your store");
            }
            
            Product product = new Product(
                    ProductId.of(request.getProductId()),
                    storeId,
                    request.getName(),
                    request.getBrand(),
                    request.getCategory(),
                    request.getPrice(),
                    request.getQuantity()
            );
            
            Product savedProduct = productRepository.save(product);
            
            logger.info("Product created successfully with ID: {} for store: {}", savedProduct.getProductId().getValue(), storeId);
            
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
