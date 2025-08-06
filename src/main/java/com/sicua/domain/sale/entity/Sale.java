package com.sicua.domain.sale.entity;

import com.sicua.domain.sale.valueobject.SaleId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sale domain entity representing a complete sale transaction
 */
public class Sale {
    private SaleId id;
    private String storeId;
    private String clientDni;
    private String clientName;
    private LocalDateTime date;
    private List<SaleItem> items;
    private BigDecimal total;
    private Boolean invoiced;
    private LocalDateTime createdAt;

    protected Sale() {
        // For frameworks
    }

    public Sale(SaleId id, String storeId, String clientDni, String clientName, List<SaleItem> items) {
        this.id = Objects.requireNonNull(id, "Sale ID cannot be null");
        this.storeId = Objects.requireNonNull(storeId, "Store ID cannot be null");
        this.clientDni = clientDni;
        this.clientName = clientName;
        
        // Use system local time - no timezone conversion
        LocalDateTime currentTime = LocalDateTime.now();
        this.date = currentTime;
        this.items = new ArrayList<>(Objects.requireNonNull(items, "Sale items cannot be null"));
        this.invoiced = false;
        this.createdAt = currentTime;
        
        validateItems();
        calculateTotal();
    }

    public void markAsInvoiced() {
        if (this.invoiced) {
            throw new IllegalStateException("Sale is already invoiced");
        }
        this.invoiced = true;
    }

    private void validateItems() {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Sale must have at least one item");
        }
    }

    private void calculateTotal() {
        this.total = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(SaleItem item) {
        Objects.requireNonNull(item, "Sale item cannot be null");
        this.items.add(item);
        calculateTotal();
    }

    // Getters
    public SaleId getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getClientDni() {
        return clientDni;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<SaleItem> getItems() {
        return new ArrayList<>(items);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    // Public setters for repository use (used when loading from database)
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
