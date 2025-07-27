package com.sicua.infrastructure.persistence.sale;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class SaleEntity {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "client_dni", length = 20)
    private String clientDni;
    
    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "invoiced", nullable = false)
    private Boolean invoiced;
    
    @Column(name = "store_id", nullable = false, length = 36)
    private String storeId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private List<SaleItemEntity> items = new ArrayList<>();

    protected SaleEntity() {
        // For JPA
    }

    public SaleEntity(String id, String storeId, String clientDni, String clientName, LocalDateTime date, 
                      BigDecimal total, Boolean invoiced, LocalDateTime createdAt) {
        this.id = id;
        this.storeId = storeId;
        this.clientDni = clientDni;
        this.clientName = clientName;
        this.date = date;
        this.total = total;
        this.invoiced = invoiced;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (invoiced == null) {
            invoiced = false;
        }
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SaleItemEntity> getItems() {
        return items;
    }

    public void setItems(List<SaleItemEntity> items) {
        this.items = items;
    }
}
