package com.sicua.domain.storeconfig.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Store configuration domain entity
 */
public class StoreConfig {
    private String id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private LocalDateTime updatedAt;

    protected StoreConfig() {
        // For frameworks
    }

    public StoreConfig(String name, String address, String email, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Store name cannot be null");
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
        
        validateEmail();
    }

    public void updateConfig(String name, String address, String email, String phone) {
        this.name = Objects.requireNonNull(name, "Store name cannot be null");
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
        
        validateEmail();
    }

    private void validateEmail() {
        if (email != null && !email.isEmpty() && !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreConfig that = (StoreConfig) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
