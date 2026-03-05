package com.nsbm.group03.employeeManagementService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsbm.group03.employeeManagementService.dto.EmployeeDTO;
import com.nsbm.group03.employeeManagementService.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private EmployeeDTO employeeDTO;
    
    @BeforeEach
    void setUp() {
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
    void testCreateEmployee_Success() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.email").value("john.doe@hotel.com"));
        
        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
    }
    
    @Test
    void testGetAllEmployees() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        when(employeeService.getAllEmployees()).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].firstName").value("John"));
        
        verify(employeeService, times(1)).getAllEmployees();
    }
    
    @Test
    void testGetEmployeeById_Found() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employeeDTO));
        
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"));
        
        verify(employeeService, times(1)).getEmployeeById(1L);
    }
    
    @Test
    void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
        
        verify(employeeService, times(1)).getEmployeeById(1L);
    }
    
    @Test
    void testGetEmployeeByEmail() throws Exception {
        when(employeeService.getEmployeeByEmail("john.doe@hotel.com"))
            .thenReturn(Optional.of(employeeDTO));
        
        mockMvc.perform(get("/api/employees/email/john.doe@hotel.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john.doe@hotel.com"));
    }
    
    @Test
    void testGetEmployeesByDepartment() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        when(employeeService.getEmployeesByDepartment("FRONT_DESK")).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees/department/FRONT_DESK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].department").value("FRONT_DESK"));
    }
    
    @Test
    void testGetEmployeesByStatus() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        when(employeeService.getEmployeesByStatus("ACTIVE")).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].status").value("ACTIVE"));
    }
    
    @Test
    void testSearchEmployeesByName() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        when(employeeService.searchEmployeesByName("John")).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees/search")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].firstName").value("John"));
    }
    
    @Test
    void testUpdateEmployee_Success() throws Exception {
        when(employeeService.updateEmployee(anyLong(), any(EmployeeDTO.class)))
            .thenReturn(employeeDTO);
        
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"));
        
        verify(employeeService, times(1)).updateEmployee(anyLong(), any(EmployeeDTO.class));
    }
    
    @Test
    void testUpdateEmployeeStatus() throws Exception {
        when(employeeService.updateEmployeeStatus(1L, "ON_LEAVE")).thenReturn(employeeDTO);
        
        mockMvc.perform(patch("/api/employees/1/status")
                .param("status", "ON_LEAVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        
        verify(employeeService, times(1)).updateEmployeeStatus(1L, "ON_LEAVE");
    }
    
    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);
        
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
        
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
