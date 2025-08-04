package com.sicua.application.product.usecase;

import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.service.ProductDomainService;
import com.sicua.domain.product.valueobject.ProductId;
import com.sicua.application.auth.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for deleting a product
 */
@Service
public class DeleteProductUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(DeleteProductUseCase.class);
    
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    private final SessionService sessionService;
    
    public DeleteProductUseCase(ProductRepository productRepository, ProductDomainService productDomainService, SessionService sessionService) {
        this.productRepository = productRepository;
        this.productDomainService = productDomainService;
        this.sessionService = sessionService;
    }
    
    @Transactional
    public void execute(String productId) {
        execute(productId, false);
    }
    
    @Transactional
    public void execute(String productId, boolean force) {
        logger.info("Deleting product with ID: {} (force: {})", productId, force);
        
        try {
            ProductId id = ProductId.of(productId);
            String storeId = sessionService.getCurrentStoreId();
            
            if (!productRepository.existsByIdAndStoreId(id, storeId)) {
                throw new IllegalArgumentException("Product not found with ID: " + productId);
            }
            
            // Business rule: Only allow deletion if product has no stock OR if force is true
            if (!force && !productDomainService.canDeleteProduct(id)) {
                throw new IllegalStateException("Cannot delete product with existing stock");
            }
            
            productRepository.deleteByIdAndStoreId(id, storeId);
            
            logger.info("Product deleted successfully: {}", productId);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Cannot delete product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }
}
