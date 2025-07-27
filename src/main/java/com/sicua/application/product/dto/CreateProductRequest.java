package com.sicua.application.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Datos para crear un nuevo producto")
public class CreateProductRequest {
    
    @Schema(description = "ID del producto (manual)", example = "P001")
    @NotBlank(message = "Product ID is required")
    private String productId;

    @Schema(description = "Nombre del producto", example = "Camiseta Polo Ralph Lauren")
    @NotBlank(message = "Product name is required")
    private String name;
    
    @Schema(description = "Marca del producto", example = "Ralph Lauren")
    private String brand;
    
    @Schema(description = "Categoría del producto (sexo - tipo prenda)", example = "Hombre - Camiseta")
    private String category;
    
    @Schema(description = "Precio del producto", example = "1299.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Schema(description = "Cantidad en stock", example = "10")
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    public CreateProductRequest() {}

    public CreateProductRequest(String productId, String name, String brand, String category, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.category = category;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
