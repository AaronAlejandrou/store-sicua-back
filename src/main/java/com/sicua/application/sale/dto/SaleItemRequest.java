package com.sicua.application.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Item individual de una venta")
public class SaleItemRequest {
    
    @Schema(description = "ID del producto", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @Schema(description = "Nombre del producto", example = "Laptop Dell Inspiron 15")
    @NotBlank(message = "Product name is required")
    private String name;
    
    @Schema(description = "Precio unitario del producto", example = "1299.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Schema(description = "Cantidad a vender", example = "2")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public SaleItemRequest() {}

    public SaleItemRequest(String productId, String name, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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
}
