package com.sicua.application.product.usecase;

import com.sicua.application.product.dto.ProductResponse;
import com.sicua.application.product.dto.UpdateProductRequest;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import com.sicua.application.auth.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for updating an existing product
 */
@Service
public class UpdateProductUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(UpdateProductUseCase.class);
    
    private final ProductRepository productRepository;
    private final SessionService sessionService;
    
    public UpdateProductUseCase(ProductRepository productRepository, SessionService sessionService) {
        this.productRepository = productRepository;
        this.sessionService = sessionService;
    }
    
    @Transactional
    public ProductResponse execute(String productId, UpdateProductRequest request) {
        logger.info("Updating product with ID: {}", productId);
        
        try {
            ProductId id = ProductId.of(productId);
            String storeId = sessionService.getCurrentStoreId();
            
            Product product = productRepository.findByIdAndStoreId(id, storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
            
            product.updateProduct(
                    request.getName(),
                    request.getBrand(),
                    request.getCategoryNumber(),
                    request.getSize(),
                    request.getPrice(),
                    request.getQuantity()
            );
            
            Product updatedProduct = productRepository.save(product);
            
            logger.info("Product updated successfully: {}", productId);
            
            return mapToResponse(updatedProduct);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Product not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }
    
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getProductId().getValue(),
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
