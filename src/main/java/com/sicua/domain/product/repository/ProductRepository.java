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
     * Find product by its ID and store ID
     * @param productId the product identifier
     * @param storeId the store identifier
     * @return Optional containing the product if found
     */
    Optional<Product> findByIdAndStoreId(ProductId productId, String storeId);
    
    /**
     * Find all products for a specific store
     * @param storeId the store identifier
     * @return List of all products for the store
     */
    List<Product> findAllByStoreId(String storeId);
    
    /**
     * Check if product exists by ID and store ID
     * @param productId the product identifier
     * @param storeId the store identifier
     * @return true if product exists
     */
    boolean existsByIdAndStoreId(ProductId productId, String storeId);
    
    /**
     * Save a product
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Delete a product by ID and store ID
     * @param productId the product identifier
     * @param storeId the store identifier
     */
    void deleteByIdAndStoreId(ProductId productId, String storeId);
    
    /**
     * Check if a product exists by its ID
     * @param productId the product identifier
     * @return true if the product exists
     */
    boolean existsById(ProductId productId);
}
