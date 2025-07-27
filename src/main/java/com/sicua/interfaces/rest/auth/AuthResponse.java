package com.sicua.interfaces.rest.auth;

public class AuthResponse {
    
    private String storeId;
    private String storeName;
    private String email;
    private String message;
    
    public AuthResponse() {}
    
    public AuthResponse(String storeId, String storeName, String email, String message) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.email = email;
        this.message = message;
    }
    
    // Getters and setters
    public String getStoreId() {
        return storeId;
    }
    
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    
    public String getStoreName() {
        return storeName;
    }
    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
