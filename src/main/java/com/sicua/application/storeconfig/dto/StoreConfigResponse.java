package com.sicua.application.storeconfig.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Configuración actual de la tienda")
public class StoreConfigResponse {
    
    @Schema(description = "ID único de la configuración", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "Nombre de la tienda", example = "SICUA Electronics Store")
    private String name;
    
    @Schema(description = "Dirección de la tienda", example = "Av. Principal 123, Lima, Perú")
    private String address;
    
    @Schema(description = "Correo electrónico de contacto", example = "contacto@sicua.com")
    private String email;
    
    @Schema(description = "Teléfono de contacto", example = "+51 999 888 777")
    private String phone;
    
    @Schema(description = "Fecha de última actualización", example = "2025-01-26T16:20:00")
    private LocalDateTime updatedAt;

    public StoreConfigResponse() {}

    public StoreConfigResponse(String id, String name, String address, String email, String phone, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
