package com.sicua.domain.category.entity;

import java.util.Objects;
import java.util.UUID;

public class CategoryId {
    private final String value;

    public CategoryId(String value) {
        this.value = Objects.requireNonNull(value, "CategoryId cannot be null");
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString());
    }

    public static CategoryId of(String value) {
        return new CategoryId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return Objects.equals(value, that.value);
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
