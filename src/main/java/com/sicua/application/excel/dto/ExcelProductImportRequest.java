package com.sicua.application.excel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO for importing product data from Excel
 */
public class ExcelProductImportRequest {
    
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @NotBlank(message = "Product name is required")
    private String nombre;
    
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be non-negative")
    private BigDecimal precio;
    
    private String talla;
    
    private String color;
    
    @NotNull(message = "Category number is required")
    private Integer categoriaNumero;
    
    private String categoriaNombre;
    
    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be non-negative")
    private Integer stock;
    
    private String marca;
    
    // Default constructor
    public ExcelProductImportRequest() {}
    
    public ExcelProductImportRequest(String productId, String nombre, BigDecimal precio, String talla, String color,
                                   Integer categoriaNumero, String categoriaNombre, Integer stock, String marca) {
        this.productId = productId;
        this.nombre = nombre;
        this.precio = precio;
        this.talla = talla;
        this.color = color;
        this.categoriaNumero = categoriaNumero;
        this.categoriaNombre = categoriaNombre;
        this.stock = stock;
        this.marca = marca;
    }
    
    // Getters and setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public String getTalla() {
        return talla;
    }
    
    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getCategoriaNumero() {
        return categoriaNumero;
    }
    
    public void setCategoriaNumero(Integer categoriaNumero) {
        this.categoriaNumero = categoriaNumero;
    }
    
    public String getCategoriaNombre() {
        return categoriaNombre;
    }
    
    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
}
