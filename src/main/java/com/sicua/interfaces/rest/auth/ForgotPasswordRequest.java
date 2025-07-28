package com.sicua.interfaces.rest.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    public ForgotPasswordRequest() {}
    
    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
    
    // Getters and setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
