package com.sicua.application.storeconfig.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para actualizar la configuración de la tienda")
public class UpdateStoreConfigRequest {
    
    @Schema(description = "Nombre de la tienda", example = "SICUA Electronics Store")
    @NotBlank(message = "Store name is required")
    private String name;
    
    @Schema(description = "Dirección de la tienda", example = "Av. Principal 123, Lima, Perú")
    private String address;
    
    @Schema(description = "Correo electrónico de contacto", example = "contacto@sicua.com")
    private String email;
    
    @Schema(description = "Teléfono de contacto", example = "+51 999 888 777")
    private String phone;

    public UpdateStoreConfigRequest() {}

    public UpdateStoreConfigRequest(String name, String address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
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
}
