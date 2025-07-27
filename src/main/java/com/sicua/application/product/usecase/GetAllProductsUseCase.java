package com.sicua.application.product.usecase;

import com.sicua.application.product.dto.ProductResponse;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for retrieving all products
 */
@Service
public class GetAllProductsUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsUseCase.class);
    
    private final ProductRepository productRepository;
    
    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponse> execute() {
        logger.info("Retrieving all products");
        
        try {
            List<Product> products = productRepository.findAll();
            
            logger.info("Retrieved {} products", products.size());
            
            return products.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products: " + e.getMessage(), e);
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
