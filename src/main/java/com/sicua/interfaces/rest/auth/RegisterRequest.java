package com.sicua.interfaces.rest.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String storeName;
    
    @NotBlank(message = "La dirección de la tienda es obligatoria")
    private String storeAddress;
    
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String storePhone;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String storeName, String storeAddress, String email, String storePhone, String password) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.email = email;
        this.storePhone = storePhone;
        this.password = password;
    }
    
    // Getters and setters
    public String getStoreName() {
        return storeName;
    }
    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    public String getStoreAddress() {
        return storeAddress;
    }
    
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStorePhone() {
        return storePhone;
    }
    
    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
