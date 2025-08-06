package com.sicua.infrastructure.persistence.product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductEntity {
    
    @Id
    @Column(name = "product_id", length = 36)
    private String productId;
    
    @Column(name = "store_id", length = 36, nullable = false)
    private String storeId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "brand", length = 100)
    private String brand;
    
    @Column(name = "category_number")
    private Integer categoryNumber;
    
    @Column(name = "size", length = 50)
    private String size;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected ProductEntity() {
        // For JPA
    }

    public ProductEntity(String productId, String storeId, String name, String brand, Integer categoryNumber, String size, BigDecimal price, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.storeId = storeId;
        this.name = name;
        this.brand = brand;
        this.categoryNumber = categoryNumber;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        // Use system local time - no timezone conversion
        LocalDateTime currentTime = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = currentTime;
        }
        if (updatedAt == null) {
            updatedAt = currentTime;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Use system local time - no timezone conversion
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
