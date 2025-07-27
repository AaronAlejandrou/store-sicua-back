package com.sicua.application.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Información de una venta")
public class SaleResponse {
    
    @Schema(description = "ID único de la venta", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "DNI del cliente", example = "12345678")
    private String clientDni;
    
    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String clientName;
    
    @Schema(description = "Fecha y hora de la venta", example = "2025-01-26T14:30:00")
    private LocalDateTime date;
    
    @Schema(description = "Lista de items vendidos")
    private List<SaleItemResponse> items;
    
    @Schema(description = "Total de la venta", example = "2599.98")
    private BigDecimal total;
    
    @Schema(description = "Indica si la venta ha sido facturada", example = "false")
    private Boolean invoiced;
    
    @Schema(description = "Fecha de creación del registro", example = "2025-01-26T14:30:00")
    private LocalDateTime createdAt;

    public SaleResponse() {}

    public SaleResponse(String id, String clientDni, String clientName, LocalDateTime date,
                       List<SaleItemResponse> items, BigDecimal total, Boolean invoiced, LocalDateTime createdAt) {
        this.id = id;
        this.clientDni = clientDni;
        this.clientName = clientName;
        this.date = date;
        this.items = items;
        this.total = total;
        this.invoiced = invoiced;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientDni() {
        return clientDni;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<SaleItemResponse> getItems() {
        return items;
    }

    public void setItems(List<SaleItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Schema(description = "Item individual de la venta")
    public static class SaleItemResponse {
        @Schema(description = "ID del producto", example = "123e4567-e89b-12d3-a456-426614174000")
        private String productId;
        
        @Schema(description = "Nombre del producto", example = "Laptop Dell Inspiron 15")
        private String name;
        
        @Schema(description = "Precio unitario", example = "1299.99")
        private BigDecimal price;
        
        @Schema(description = "Cantidad vendida", example = "2")
        private Integer quantity;
        
        @Schema(description = "Subtotal del item", example = "2599.98")
        private BigDecimal subtotal;

        public SaleItemResponse() {}

        public SaleItemResponse(String productId, String name, BigDecimal price, Integer quantity, BigDecimal subtotal) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
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

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
}
