package com.sicua.application.product.usecase;

import com.sicua.application.auth.SessionService;
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
    private final SessionService sessionService;
    
    public GetAllProductsUseCase(ProductRepository productRepository, SessionService sessionService) {
        this.productRepository = productRepository;
        this.sessionService = sessionService;
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponse> execute() {
        String storeId = sessionService.getCurrentStoreId();
        logger.info("Retrieving all products for store: {}", storeId);
        
        try {
            List<Product> products = productRepository.findAllByStoreId(storeId);
            
            List<ProductResponse> response = products.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
            logger.info("Retrieved {} products for store: {}", response.size(), storeId);
            return response;
            
        } catch (Exception e) {
            logger.error("Error retrieving products for store {}: {}", storeId, e.getMessage(), e);
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
