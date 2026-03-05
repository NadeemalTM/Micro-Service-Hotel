package com.nsbm.group03.employeeManagementService.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthValidateRequest {
    
    @NotBlank(message = "Token is required")
    private String token;
    
    // Constructors
    public AuthValidateRequest() {
    }
    
    public AuthValidateRequest(String token) {
        this.token = token;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
