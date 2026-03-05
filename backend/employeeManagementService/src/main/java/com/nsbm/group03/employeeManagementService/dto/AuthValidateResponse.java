package com.nsbm.group03.employeeManagementService.dto;

public class AuthValidateResponse {
    
    private Boolean valid;
    private String username;
    private String email;
    private String role;
    private Long employeeId;
    private String message;
    
    // Constructors
    public AuthValidateResponse() {
    }
    
    public AuthValidateResponse(Boolean valid, String username, String email, 
                                String role, Long employeeId, String message) {
        this.valid = valid;
        this.username = username;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
        this.message = message;
    }
    
    // Getters and Setters
    public Boolean getValid() {
        return valid;
    }
    
    public void setValid(Boolean valid) {
        this.valid = valid;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
