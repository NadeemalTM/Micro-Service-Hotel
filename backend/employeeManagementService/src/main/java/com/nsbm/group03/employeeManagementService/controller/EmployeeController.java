package com.nsbm.group03.employeeManagementService.controller;

import com.nsbm.group03.employeeManagementService.dto.DepartmentStatisticsDTO;
import com.nsbm.group03.employeeManagementService.dto.EmployeeDTO;
import com.nsbm.group03.employeeManagementService.dto.EmployeeStatisticsDTO;
import com.nsbm.group03.employeeManagementService.response.ApiResponse;
import com.nsbm.group03.employeeManagementService.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Tag(name = "Employee Management", description = "APIs for managing hotel employees")
public class EmployeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    @Operation(summary = "Create a new employee", description = "Creates a new employee record in the system")
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        logger.info("REST request to create employee: {}", employeeDTO.getEmail());
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(
            ApiResponse.success("Employee created successfully", createdEmployee), 
            HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        logger.info("REST request to get all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(
            ApiResponse.success("Employees retrieved successfully", employees), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        logger.info("REST request to get employee by id: {}", id);
        return employeeService.getEmployeeById(id)
                .map(employee -> new ResponseEntity<>(
                    ApiResponse.success("Employee retrieved successfully", employee), 
                    HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                    ApiResponse.error("Employee not found with id: " + id), 
                    HttpStatus.NOT_FOUND
                ));
    }
    
    @Operation(summary = "Get employee by email", description = "Retrieves an employee by their email address")
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByEmail(@PathVariable String email) {
        logger.info("REST request to get employee by email: {}", email);
        return employeeService.getEmployeeByEmail(email)
                .map(employee -> new ResponseEntity<>(
                    ApiResponse.success("Employee retrieved successfully", employee), 
                    HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                    ApiResponse.error("Employee not found with email: " + email), 
                    HttpStatus.NOT_FOUND
                ));
    }
    
    @Operation(summary = "Get employees by department", description = "Retrieves all employees in a specific department")
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable String department) {
        logger.info("REST request to get employees by department: {}", department);
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
        return new ResponseEntity<>(
            ApiResponse.success("Employees retrieved successfully", employees), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get employees by status", description = "Retrieves all employees with a specific status")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByStatus(@PathVariable String status) {
        logger.info("REST request to get employees by status: {}", status);
        List<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status);
        return new ResponseEntity<>(
            ApiResponse.success("Employees retrieved successfully", employees), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get employees by position", description = "Retrieves all employees with a specific position")
    @GetMapping("/position/{position}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByPosition(@PathVariable String position) {
        logger.info("REST request to get employees by position: {}", position);
        List<EmployeeDTO> employees = employeeService.getEmployeesByPosition(position);
        return new ResponseEntity<>(
            ApiResponse.success("Employees retrieved successfully", employees), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Search employees by name", description = "Searches for employees by first or last name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> searchEmployeesByName(@RequestParam String name) {
        logger.info("REST request to search employees by name: {}", name);
        List<EmployeeDTO> employees = employeeService.searchEmployeesByName(name);
        return new ResponseEntity<>(
            ApiResponse.success("Search completed successfully", employees), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Update an employee", description = "Updates an existing employee's information")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id, 
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        logger.info("REST request to update employee with id: {}", id);
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return new ResponseEntity<>(
            ApiResponse.success("Employee updated successfully", updatedEmployee), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Update employee status", description = "Updates the status of an employee")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployeeStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        logger.info("REST request to update status for employee with id: {} to: {}", id, status);
        EmployeeDTO updatedEmployee = employeeService.updateEmployeeStatus(id, status);
        return new ResponseEntity<>(
            ApiResponse.success("Employee status updated successfully", updatedEmployee), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Delete an employee", description = "Deletes an employee from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        logger.info("REST request to delete employee with id: {}", id);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(
            ApiResponse.success("Employee deleted successfully", null), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get employee statistics", description = "Retrieves overall employee statistics")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<EmployeeStatisticsDTO>> getEmployeeStatistics() {
        logger.info("REST request to get employee statistics");
        EmployeeStatisticsDTO statistics = employeeService.getEmployeeStatistics();
        return new ResponseEntity<>(
            ApiResponse.success("Statistics retrieved successfully", statistics), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get department statistics", description = "Retrieves statistics for all departments")
    @GetMapping("/statistics/departments")
    public ResponseEntity<ApiResponse<List<DepartmentStatisticsDTO>>> getDepartmentStatistics() {
        logger.info("REST request to get department statistics");
        List<DepartmentStatisticsDTO> statistics = employeeService.getDepartmentStatistics();
        return new ResponseEntity<>(
            ApiResponse.success("Department statistics retrieved successfully", statistics), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get employee count", description = "Retrieves total count of all employees")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getEmployeeCount() {
        logger.info("REST request to get employee count");
        long count = employeeService.getEmployeeCount();
        return new ResponseEntity<>(
            ApiResponse.success("Employee count retrieved successfully", count), 
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Get active employee count", description = "Retrieves count of active employees")
    @GetMapping("/count/active")
    public ResponseEntity<ApiResponse<Long>> getActiveEmployeeCount() {
        logger.info("REST request to get active employee count");
        long count = employeeService.getActiveEmployeeCount();
        return new ResponseEntity<>(
            ApiResponse.success("Active employee count retrieved successfully", count), 
            HttpStatus.OK
        );
    }
}
