package com.sicua.domain.product.repository;

import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product domain entity
 */
public interface ProductRepository {
    
    /**
     * Find product by its ID
     * @param productId the product identifier
     * @return Optional containing the product if found
     */
    Optional<Product> findById(ProductId productId);
    
    /**
     * Find all products
     * @return List of all products
     */
    List<Product> findAll();
    
    /**
     * Save a product
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Delete a product by its ID
     * @param productId the product identifier
     */
    void deleteById(ProductId productId);
    
    /**
     * Check if a product exists by its ID
     * @param productId the product identifier
     * @return true if the product exists
     */
    boolean existsById(ProductId productId);
}
