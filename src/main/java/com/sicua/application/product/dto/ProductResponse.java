package com.sicua.application.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información de un producto")
public class ProductResponse {
    
    @Schema(description = "ID único del producto", example = "123e4567-e89b-12d3-a456-426614174000")
    private String productId;
    
    @Schema(description = "Nombre del producto", example = "Camiseta Polo Ralph Lauren")
    private String name;
    
    @Schema(description = "Marca del producto", example = "Ralph Lauren")
    private String brand;
    
    @Schema(description = "Número de categoría del producto", example = "1")
    private Integer categoryNumber;
    
    @Schema(description = "Talla del producto", example = "M")
    private String size;
    
    @Schema(description = "Precio del producto", example = "1299.99")
    private BigDecimal price;
    
    @Schema(description = "Cantidad disponible en stock", example = "10")
    private Integer quantity;
    
    @Schema(description = "Fecha de creación del producto", example = "2025-01-26T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Fecha de última actualización", example = "2025-01-26T15:45:00")
    private LocalDateTime updatedAt;

    public ProductResponse() {}

    public ProductResponse(String productId, String name, String brand, Integer categoryNumber, String size, BigDecimal price, 
                          Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.categoryNumber = categoryNumber;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
