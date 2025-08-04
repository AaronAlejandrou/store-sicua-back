package com.sicua.infrastructure.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, String> {
    
    Optional<CategoryEntity> findByCategoryIdAndStoreId(String categoryId, String storeId);
    
    List<CategoryEntity> findByStoreIdOrderByCategoryNumber(String storeId);
    
    Optional<CategoryEntity> findByCategoryNumberAndStoreId(Integer categoryNumber, String storeId);
    
    Optional<CategoryEntity> findByNameAndStoreId(String name, String storeId);
    
    void deleteByCategoryIdAndStoreId(String categoryId, String storeId);
    
    boolean existsByCategoryIdAndStoreId(String categoryId, String storeId);
    
    boolean existsByCategoryNumberAndStoreIdAndCategoryIdNot(Integer categoryNumber, String storeId, String excludeCategoryId);
    
    boolean existsByNameAndStoreIdAndCategoryIdNot(String name, String storeId, String excludeCategoryId);
    
    @Query("SELECT COALESCE(MAX(c.categoryNumber), 0) + 1 FROM CategoryEntity c WHERE c.storeId = :storeId")
    Integer findNextCategoryNumber(@Param("storeId") String storeId);
}
