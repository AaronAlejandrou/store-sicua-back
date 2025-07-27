package com.sicua.domain.product.service;

import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Domain service for Product business logic
 */
@Service
public class ProductDomainService {
    
    private final ProductRepository productRepository;
    
    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Validates if a product can be deleted
     * @param productId the product identifier
     * @return true if the product can be deleted
     */
    public boolean canDeleteProduct(ProductId productId) {
        return productRepository.findById(productId)
                .map(product -> product.getQuantity() == 0)
                .orElse(false);
    }
    
    /**
     * Validates stock availability for multiple products
     * @param products list of products to validate
     * @return true if all products have enough stock
     */
    public boolean validateStockAvailability(List<Product> products) {
        return products.stream()
                .allMatch(product -> product.getQuantity() > 0);
    }
    
    /**
     * Checks if a product name already exists (case insensitive)
     * @param name the product name to check
     * @param excludeProductId product ID to exclude from the check (for updates)
     * @return true if the name already exists
     */
    public boolean isProductNameExists(String name, ProductId excludeProductId) {
        return productRepository.findAll().stream()
                .filter(product -> !product.getProductId().equals(excludeProductId))
                .anyMatch(product -> product.getName().equalsIgnoreCase(name));
    }
}
