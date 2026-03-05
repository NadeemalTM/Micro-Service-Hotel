package com.nsbm.group03.employeeManagementService.service;

import com.nsbm.group03.employeeManagementService.dto.*;
import com.nsbm.group03.employeeManagementService.entity.Employee;
import com.nsbm.group03.employeeManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.employeeManagementService.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationService(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }
    
    @Transactional
    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        
        // Find employee
        Employee employee = employeeRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        
        // Check if account is locked
        if (employee.getAccountLocked()) {
            logger.warn("Login attempt for locked account: {}", request.getUsername());
            throw new BadCredentialsException("Account is locked due to multiple failed login attempts");
        }
        
        try {
            // Authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            // Reset login attempts on successful login
            employee.setLoginAttempts(0);
            employee.setLastLogin(LocalDateTime.now());
            employeeRepository.save(employee);
            
            // Generate token
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);
            
            logger.info("Login successful for username: {}", request.getUsername());
            
            return new LoginResponse(
                    token,
                    employee.getUsername(),
                    employee.getEmail(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getRole(),
                    employee.getId()
            );
            
        } catch (BadCredentialsException e) {
            // Increment login attempts
            employee.setLoginAttempts(employee.getLoginAttempts() + 1);
            
            // Lock account if max attempts reached
            if (employee.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                employee.setAccountLocked(true);
                logger.warn("Account locked due to multiple failed attempts: {}", request.getUsername());
            }
            
            employeeRepository.save(employee);
            logger.warn("Failed login attempt for username: {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    
    public AuthValidateResponse validateToken(AuthValidateRequest request) {
        try {
            String username = jwtService.extractUsername(request.getToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(request.getToken(), userDetails)) {
                Employee employee = userDetailsService.loadEmployeeByUsername(username);
                
                return new AuthValidateResponse(
                        true,
                        employee.getUsername(),
                        employee.getEmail(),
                        employee.getRole(),
                        employee.getId(),
                        "Token is valid"
                );
            } else {
                return new AuthValidateResponse(
                        false,
                        null,
                        null,
                        null,
                        null,
                        "Token is invalid or expired"
                );
            }
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return new AuthValidateResponse(
                    false,
                    null,
                    null,
                    null,
                    null,
                    "Token validation failed: " + e.getMessage()
            );
        }
    }
    
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        
        // Update password
        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);
        
        logger.info("Password changed successfully for username: {}", username);
    }
    
    @Transactional
    public void unlockAccount(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        employee.setAccountLocked(false);
        employee.setLoginAttempts(0);
        employeeRepository.save(employee);
        
        logger.info("Account unlocked for employee: {}", employee.getUsername());
    }
}
