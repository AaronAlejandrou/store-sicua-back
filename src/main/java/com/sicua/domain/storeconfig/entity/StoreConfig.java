package com.sicua.domain.storeconfig.entity;

import org.mindrot.jbcrypt.BCrypt;
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
    private String password;
    private LocalDateTime createdAt;
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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateEmail();
    }

    public StoreConfig(String name, String address, String email, String phone, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Store name cannot be null");
        this.address = address;
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phone = phone;
        this.setPassword(password);
        this.createdAt = LocalDateTime.now();
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

    // Authentication methods
    public void setPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.password = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean validatePassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
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

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
