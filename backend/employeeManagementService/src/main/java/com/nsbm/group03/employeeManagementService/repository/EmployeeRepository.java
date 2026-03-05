package com.nsbm.group03.employeeManagementService.repository;

import com.nsbm.group03.employeeManagementService.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByUsername(String username);
    
    List<Employee> findByDepartment(String department);
    
    List<Employee> findByStatus(String status);
    
    List<Employee> findByPosition(String position);
    
    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    
    boolean existsByEmail(String email);
}
