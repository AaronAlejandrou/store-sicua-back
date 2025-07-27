package com.sicua.domain.product.entity;

import com.sicua.domain.product.valueobject.ProductId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product domain entity representing a product in the store inventory
 */
public class Product {
    private ProductId productId;
    private String name;
    private String brand;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Product() {
        // For frameworks
    }

    public Product(ProductId productId, String name, String brand, String category, BigDecimal price, Integer quantity) {
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.brand = brand;
        this.category = category;
        this.price = Objects.requireNonNull(price, "Product price cannot be null");
        this.quantity = Objects.requireNonNull(quantity, "Product quantity cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validatePrice();
        validateQuantity();
    }

    public void updateProduct(String name, String brand, String category, BigDecimal price, Integer quantity) {
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.brand = brand;
        this.category = category;
        this.price = Objects.requireNonNull(price, "Product price cannot be null");
        this.quantity = Objects.requireNonNull(quantity, "Product quantity cannot be null");
        this.updatedAt = LocalDateTime.now();
        
        validatePrice();
        validateQuantity();
    }

    public void reduceStock(Integer quantityToReduce) {
        if (quantityToReduce == null || quantityToReduce <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be positive");
        }
        if (this.quantity < quantityToReduce) {
            throw new IllegalStateException("Insufficient stock. Available: " + this.quantity + ", Required: " + quantityToReduce);
        }
        this.quantity -= quantityToReduce;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasEnoughStock(Integer requiredQuantity) {
        return this.quantity >= requiredQuantity;
    }

    private void validatePrice() {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

    private void validateQuantity() {
        if (quantity < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
    }

    // Getters
    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
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
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
