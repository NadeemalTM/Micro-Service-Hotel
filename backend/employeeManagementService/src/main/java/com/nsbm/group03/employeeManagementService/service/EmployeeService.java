package com.nsbm.group03.employeeManagementService.service;

import com.nsbm.group03.employeeManagementService.dto.DepartmentStatisticsDTO;
import com.nsbm.group03.employeeManagementService.dto.EmployeeDTO;
import com.nsbm.group03.employeeManagementService.dto.EmployeeStatisticsDTO;
import com.nsbm.group03.employeeManagementService.entity.Employee;
import com.nsbm.group03.employeeManagementService.exception.DuplicateResourceException;
import com.nsbm.group03.employeeManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.employeeManagementService.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // Convert Entity to DTO
    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getPhone(),
            employee.getPosition(),
            employee.getDepartment(),
            employee.getSalary(),
            employee.getHireDate(),
            employee.getStatus(),
            employee.getAddress()
        );
    }
    
    // Convert DTO to Entity
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setPosition(dto.getPosition());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());
        employee.setStatus(dto.getStatus());
        employee.setAddress(dto.getAddress());
        return employee;
    }
    
    // Create a new employee
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.info("Creating new employee with email: {}", employeeDTO.getEmail());
        
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            logger.error("Employee with email {} already exists", employeeDTO.getEmail());
            throw new DuplicateResourceException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Successfully created employee with id: {}", savedEmployee.getId());
        return convertToDTO(savedEmployee);
    }
    
    // Get all employees
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        logger.info("Fetching all employees");
        List<EmployeeDTO> employees = employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} employees", employees.size());
        return employees;
    }
    
    // Get employee by ID
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        logger.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Get employee by email
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeByEmail(String email) {
        logger.info("Fetching employee with email: {}", email);
        return employeeRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    // Get employees by department
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        logger.info("Fetching employees in department: {}", department);
        List<EmployeeDTO> employees = employeeRepository.findByDepartment(department)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} employees in department: {}", employees.size(), department);
        return employees;
    }
    
    // Get employees by status
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByStatus(String status) {
        logger.info("Fetching employees with status: {}", status);
        List<EmployeeDTO> employees = employeeRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} employees with status: {}", employees.size(), status);
        return employees;
    }
    
    // Get employees by position
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByPosition(String position) {
        logger.info("Fetching employees with position: {}", position);
        List<EmployeeDTO> employees = employeeRepository.findByPosition(position)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} employees with position: {}", employees.size(), position);
        return employees;
    }
    
    // Search employees by name
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployeesByName(String searchTerm) {
        logger.info("Searching employees with name containing: {}", searchTerm);
        List<EmployeeDTO> employees = employeeRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} employees matching search term: {}", employees.size(), searchTerm);
        return employees;
    }
    
    // Update an employee
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        logger.info("Updating employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        
        // Check if email is being changed and if it already exists
        if (!employee.getEmail().equals(employeeDTO.getEmail()) && 
            employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            logger.error("Employee with email {} already exists", employeeDTO.getEmail());
            throw new DuplicateResourceException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setPosition(employeeDTO.getPosition());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setSalary(employeeDTO.getSalary());
        employee.setHireDate(employeeDTO.getHireDate());
        employee.setStatus(employeeDTO.getStatus());
        employee.setAddress(employeeDTO.getAddress());
        
        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Successfully updated employee with id: {}", id);
        return convertToDTO(updatedEmployee);
    }
    
    // Delete an employee
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with id: {}", id);
        
        if (!employeeRepository.existsById(id)) {
            logger.error("Employee not found with id: {}", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        
        employeeRepository.deleteById(id);
        logger.info("Successfully deleted employee with id: {}", id);
    }
    
    // Update employee status
    public EmployeeDTO updateEmployeeStatus(Long id, String status) {
        logger.info("Updating status for employee with id: {} to: {}", id, status);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        
        employee.setStatus(status);
        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Successfully updated status for employee with id: {}", id);
        return convertToDTO(updatedEmployee);
    }
    
    // Get employee statistics
    @Transactional(readOnly = true)
    public EmployeeStatisticsDTO getEmployeeStatistics() {
        logger.info("Calculating employee statistics");
        
        List<Employee> allEmployees = employeeRepository.findAll();
        
        long totalEmployees = allEmployees.size();
        long activeEmployees = allEmployees.stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .count();
        long inactiveEmployees = allEmployees.stream()
                .filter(e -> "INACTIVE".equalsIgnoreCase(e.getStatus()))
                .count();
        long onLeaveEmployees = allEmployees.stream()
                .filter(e -> "ON_LEAVE".equalsIgnoreCase(e.getStatus()))
                .count();
        
        Double averageSalary = allEmployees.isEmpty() ? 0.0 : 
                allEmployees.stream()
                        .mapToDouble(Employee::getSalary)
                        .average()
                        .orElse(0.0);
        
        Double totalSalaryExpense = allEmployees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        
        EmployeeStatisticsDTO stats = new EmployeeStatisticsDTO(
                totalEmployees,
                activeEmployees,
                inactiveEmployees,
                onLeaveEmployees,
                averageSalary,
                totalSalaryExpense
        );
        
        logger.info("Statistics calculated: {} total employees", totalEmployees);
        return stats;
    }
    
    // Get department statistics
    @Transactional(readOnly = true)
    public List<DepartmentStatisticsDTO> getDepartmentStatistics() {
        logger.info("Calculating department statistics");
        
        List<Employee> allEmployees = employeeRepository.findAll();
        
        Map<String, List<Employee>> employeesByDepartment = allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        
        List<DepartmentStatisticsDTO> departmentStats = employeesByDepartment.entrySet().stream()
                .map(entry -> {
                    String department = entry.getKey();
                    List<Employee> employees = entry.getValue();
                    
                    long employeeCount = employees.size();
                    double averageSalary = employees.stream()
                            .mapToDouble(Employee::getSalary)
                            .average()
                            .orElse(0.0);
                    double totalSalary = employees.stream()
                            .mapToDouble(Employee::getSalary)
                            .sum();
                    long activeCount = employees.stream()
                            .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                            .count();
                    
                    return new DepartmentStatisticsDTO(
                            department,
                            employeeCount,
                            averageSalary,
                            totalSalary,
                            activeCount
                    );
                })
                .collect(Collectors.toList());
        
        logger.info("Department statistics calculated for {} departments", departmentStats.size());
        return departmentStats;
    }
    
    // Get employee count
    @Transactional(readOnly = true)
    public long getEmployeeCount() {
        logger.info("Getting total employee count");
        long count = employeeRepository.count();
        logger.info("Total employee count: {}", count);
        return count;
    }
    
    // Get active employee count
    @Transactional(readOnly = true)
    public long getActiveEmployeeCount() {
        logger.info("Getting active employee count");
        long count = employeeRepository.findByStatus("ACTIVE").size();
        logger.info("Active employee count: {}", count);
        return count;
    }
}
