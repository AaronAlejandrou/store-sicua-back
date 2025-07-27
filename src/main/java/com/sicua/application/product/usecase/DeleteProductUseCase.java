package com.sicua.application.product.usecase;

import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.service.ProductDomainService;
import com.sicua.domain.product.valueobject.ProductId;
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
    
    public DeleteProductUseCase(ProductRepository productRepository, ProductDomainService productDomainService) {
        this.productRepository = productRepository;
        this.productDomainService = productDomainService;
    }
    
    @Transactional
    public void execute(String productId) {
        logger.info("Deleting product with ID: {}", productId);
        
        try {
            ProductId id = ProductId.of(productId);
            
            if (!productRepository.existsById(id)) {
                throw new IllegalArgumentException("Product not found with ID: " + productId);
            }
            
            // Business rule: Only allow deletion if product has no stock
            // This is a simplified rule - in real scenarios you might check for pending orders, etc.
            if (!productDomainService.canDeleteProduct(id)) {
                throw new IllegalStateException("Cannot delete product with existing stock");
            }
            
            productRepository.deleteById(id);
            
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
