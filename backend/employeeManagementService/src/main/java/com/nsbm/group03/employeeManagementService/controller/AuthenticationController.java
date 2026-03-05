package com.nsbm.group03.employeeManagementService.controller;

import com.nsbm.group03.employeeManagementService.dto.*;
import com.nsbm.group03.employeeManagementService.response.ApiResponse;
import com.nsbm.group03.employeeManagementService.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AuthenticationController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    private final AuthenticationService authenticationService;
    
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("REST request to login user: {}", request.getUsername());
        
        try {
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            logger.error("Login failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate Token", description = "Validate JWT token and return user information")
    public ResponseEntity<ApiResponse<AuthValidateResponse>> validateToken(
            @Valid @RequestBody AuthValidateRequest request) {
        logger.info("REST request to validate token");
        
        AuthValidateResponse response = authenticationService.validateToken(request);
        
        if (response.getValid()) {
            return ResponseEntity.ok(ApiResponse.success("Token is valid", response));
        } else {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(response.getMessage()));
        }
    }
    
    @PostMapping("/change-password")
    @Operation(summary = "Change Password", description = "Change password for authenticated user")
    public ResponseEntity<ApiResponse<String>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        logger.info("REST request to change password for user: {}", authentication.getName());
        
        try {
            authenticationService.changePassword(authentication.getName(), request);
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
        } catch (Exception e) {
            logger.error("Failed to change password for user: {}", authentication.getName(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/unlock-account/{employeeId}")
    @Operation(summary = "Unlock Account", description = "Unlock a locked employee account (Admin only)")
    public ResponseEntity<ApiResponse<String>> unlockAccount(@PathVariable Long employeeId) {
        logger.info("REST request to unlock account for employee ID: {}", employeeId);
        
        try {
            authenticationService.unlockAccount(employeeId);
            return ResponseEntity.ok(ApiResponse.success("Account unlocked successfully"));
        } catch (Exception e) {
            logger.error("Failed to unlock account for employee ID: {}", employeeId, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get Current User", description = "Get current authenticated user information")
    public ResponseEntity<ApiResponse<String>> getCurrentUser(Authentication authentication) {
        logger.info("REST request to get current user");
        
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponse.success("Current user retrieved", authentication.getName()));
        }
        
        return ResponseEntity.status(401)
                .body(ApiResponse.error("User not authenticated"));
    }
}
