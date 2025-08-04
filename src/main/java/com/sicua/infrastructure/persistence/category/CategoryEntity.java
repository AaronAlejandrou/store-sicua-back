package com.sicua.infrastructure.persistence.category;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"store_id", "category_number"}),
    @UniqueConstraint(columnNames = {"store_id", "name"})
})
public class CategoryEntity {
    
    @Id
    @Column(name = "category_id", length = 36)
    private String categoryId;
    
    @Column(name = "store_id", length = 36, nullable = false)
    private String storeId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "category_number", nullable = false)
    private Integer categoryNumber;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CategoryEntity() {
        // For JPA
    }

    public CategoryEntity(String categoryId, String storeId, String name, Integer categoryNumber, 
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.name = name;
        this.categoryNumber = categoryNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
