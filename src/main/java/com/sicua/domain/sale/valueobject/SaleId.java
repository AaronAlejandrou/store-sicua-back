package com.sicua.domain.sale.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SaleId {
    private final String value;

    protected SaleId() {
        this.value = null; // For JPA
    }

    public SaleId(String value) {
        this.value = Objects.requireNonNull(value, "Sale ID cannot be null");
    }

    public static SaleId generate() {
        return new SaleId(UUID.randomUUID().toString());
    }

    public static SaleId of(String value) {
        return new SaleId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleId saleId = (SaleId) o;
        return Objects.equals(value, saleId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
