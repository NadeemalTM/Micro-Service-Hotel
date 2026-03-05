package com.nsbm.group03.employeeManagementService.service;

import com.nsbm.group03.employeeManagementService.dto.EmployeeDTO;
import com.nsbm.group03.employeeManagementService.entity.Employee;
import com.nsbm.group03.employeeManagementService.exception.DuplicateResourceException;
import com.nsbm.group03.employeeManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.employeeManagementService.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private EmployeeService employeeService;
    
    private Employee employee;
    private EmployeeDTO employeeDTO;
    
    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@hotel.com");
        employee.setPhone("1234567890");
        employee.setPosition("Manager");
        employee.setDepartment("FRONT_DESK");
        employee.setSalary(50000.0);
        employee.setHireDate(LocalDate.of(2023, 1, 15));
        employee.setStatus("ACTIVE");
        employee.setAddress("123 Main St");
        
        employeeDTO = new EmployeeDTO(
            1L,
            "John",
            "Doe",
            "john.doe@hotel.com",
            "1234567890",
            "Manager",
            "FRONT_DESK",
            50000.0,
            LocalDate.of(2023, 1, 15),
            "ACTIVE",
            "123 Main St"
        );
    }
    
    @Test
    void testCreateEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        EmployeeDTO result = employeeService.createEmployee(employeeDTO);
        
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@hotel.com", result.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testCreateEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);
        
        assertThrows(DuplicateResourceException.class, () -> {
            employeeService.createEmployee(employeeDTO);
        });
        
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        
        List<EmployeeDTO> result = employeeService.getAllEmployees();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }
    
    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(1L);
        
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetEmployeeByEmail() {
        when(employeeRepository.findByEmail("john.doe@hotel.com")).thenReturn(Optional.of(employee));
        
        Optional<EmployeeDTO> result = employeeService.getEmployeeByEmail("john.doe@hotel.com");
        
        assertTrue(result.isPresent());
        assertEquals("john.doe@hotel.com", result.get().getEmail());
    }
    
    @Test
    void testGetEmployeesByDepartment() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findByDepartment("FRONT_DESK")).thenReturn(employees);
        
        List<EmployeeDTO> result = employeeService.getEmployeesByDepartment("FRONT_DESK");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FRONT_DESK", result.get(0).getDepartment());
    }
    
    @Test
    void testGetEmployeesByStatus() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findByStatus("ACTIVE")).thenReturn(employees);
        
        List<EmployeeDTO> result = employeeService.getEmployeesByStatus("ACTIVE");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }
    
    @Test
    void testUpdateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        EmployeeDTO updatedDTO = new EmployeeDTO(
            1L, "Jane", "Doe", "john.doe@hotel.com", "9876543210",
            "Senior Manager", "MANAGEMENT", 60000.0,
            LocalDate.of(2023, 1, 15), "ACTIVE", "456 Oak Ave"
        );
        
        EmployeeDTO result = employeeService.updateEmployee(1L, updatedDTO);
        
        assertNotNull(result);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(1L, employeeDTO);
        });
        
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee_DuplicateEmail() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setEmail("old.email@hotel.com");
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.existsByEmail("john.doe@hotel.com")).thenReturn(true);
        
        assertThrows(DuplicateResourceException.class, () -> {
            employeeService.updateEmployee(1L, employeeDTO);
        });
    }
    
    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        
        verify(employeeRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(1L);
        });
        
        verify(employeeRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void testUpdateEmployeeStatus_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        EmployeeDTO result = employeeService.updateEmployeeStatus(1L, "ON_LEAVE");
        
        assertNotNull(result);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployeeStatus_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployeeStatus(1L, "ON_LEAVE");
        });
    }
    
    @Test
    void testSearchEmployeesByName() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findByFirstNameContainingOrLastNameContaining("John", "John"))
            .thenReturn(employees);
        
        List<EmployeeDTO> result = employeeService.searchEmployeesByName("John");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getFirstName().contains("John"));
    }
}
