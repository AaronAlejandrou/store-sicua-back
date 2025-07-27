package com.sicua.domain.sale.entity;

import com.sicua.domain.product.valueobject.ProductId;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Sale item domain entity representing an item within a sale
 */
public class SaleItem {
    private ProductId productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    protected SaleItem() {
        // For frameworks
    }

    public SaleItem(ProductId productId, String name, BigDecimal price, Integer quantity) {
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
        
        validatePrice();
        validateQuantity();
        calculateSubtotal();
    }

    private void validatePrice() {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    private void validateQuantity() {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private void calculateSubtotal() {
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters
    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return Objects.equals(productId, saleItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
