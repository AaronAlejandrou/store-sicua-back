package com.sicua.domain.category.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Category {
    private final CategoryId categoryId;
    private final String storeId;
    private String name;
    private Integer categoryNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category(CategoryId categoryId, String storeId, String name, Integer categoryNumber) {
        this.categoryId = Objects.requireNonNull(categoryId, "CategoryId cannot be null");
        this.storeId = Objects.requireNonNull(storeId, "StoreId cannot be null");
        this.name = Objects.requireNonNull(name, "Category name cannot be null");
        this.categoryNumber = Objects.requireNonNull(categoryNumber, "Category number cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateCategoryNumber(categoryNumber);
        validateName(name);
    }

    public Category(CategoryId categoryId, String storeId, String name, Integer categoryNumber, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = Objects.requireNonNull(categoryId, "CategoryId cannot be null");
        this.storeId = Objects.requireNonNull(storeId, "StoreId cannot be null");
        this.name = Objects.requireNonNull(name, "Category name cannot be null");
        this.categoryNumber = Objects.requireNonNull(categoryNumber, "Category number cannot be null");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
        validateCategoryNumber(categoryNumber);
        validateName(name);
    }

    public void updateCategory(String name, Integer categoryNumber) {
        validateName(name);
        validateCategoryNumber(categoryNumber);
        
        this.name = name;
        this.categoryNumber = categoryNumber;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");
        }
    }

    private void validateCategoryNumber(Integer categoryNumber) {
        if (categoryNumber == null || categoryNumber <= 0) {
            throw new IllegalArgumentException("Category number must be a positive integer");
        }
    }

    // Getters
    public CategoryId getCategoryId() {
        return categoryId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", storeId='" + storeId + '\'' +
                ", name='" + name + '\'' +
                ", categoryNumber=" + categoryNumber +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
