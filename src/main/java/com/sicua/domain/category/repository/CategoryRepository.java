package com.sicua.domain.category.repository;

import com.sicua.domain.category.entity.Category;
import com.sicua.domain.category.entity.CategoryId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    
    /**
     * Save or update a category
     */
    Category save(Category category);
    
    /**
     * Find a category by its ID and store ID
     */
    Optional<Category> findByIdAndStoreId(CategoryId categoryId, String storeId);
    
    /**
     * Find all categories for a specific store
     */
    List<Category> findByStoreId(String storeId);
    
    /**
     * Find a category by category number and store ID
     */
    Optional<Category> findByCategoryNumberAndStoreId(Integer categoryNumber, String storeId);
    
    /**
     * Find a category by name and store ID
     */
    Optional<Category> findByNameAndStoreId(String name, String storeId);
    
    /**
     * Delete a category by ID and store ID
     */
    void deleteByIdAndStoreId(CategoryId categoryId, String storeId);
    
    /**
     * Check if a category exists by ID and store ID
     */
    boolean existsByIdAndStoreId(CategoryId categoryId, String storeId);
    
    /**
     * Check if a category number exists for a store (excluding specific category ID)
     */
    boolean existsByCategoryNumberAndStoreIdAndIdNot(Integer categoryNumber, String storeId, CategoryId excludeCategoryId);
    
    /**
     * Check if a category name exists for a store (excluding specific category ID)
     */
    boolean existsByNameAndStoreIdAndIdNot(String name, String storeId, CategoryId excludeCategoryId);
    
    /**
     * Get the next available category number for a store
     */
    Integer getNextCategoryNumber(String storeId);
}
