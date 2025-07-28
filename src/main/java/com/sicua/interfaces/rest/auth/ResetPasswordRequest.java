package com.sicua.interfaces.rest.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;
    
    public ResetPasswordRequest() {}
    
    public ResetPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
    
    // Getters and setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
