package com.nsbm.group03.employeeManagementService.config;

import com.nsbm.group03.employeeManagementService.entity.Employee;
import com.nsbm.group03.employeeManagementService.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Always ensure admin user exists
        if (employeeRepository.findByUsername("admin").isEmpty()) {
            logger.info("Creating admin user...");
            Employee admin = createEmployeeWithPassword("Admin", "User", "admin@hotel.com", "0770000000",
                "System Administrator", "MANAGEMENT", 150000.0, 
                LocalDate.of(2019, 1, 1), "ACTIVE", "123 Main Street, Colombo",
                "admin", "ADMIN", "admin123");
            employeeRepository.save(admin);
            logger.info("=====================================");
            logger.info("Admin user created successfully!");
            logger.info("  Username: admin");
            logger.info("  Password: admin123");
            logger.info("  Email: admin@hotel.com");
            logger.info("=====================================");
        } else {
            logger.info("Admin user already exists");
        }
        
        // Check if database is already initialized with sample data
        if (employeeRepository.count() > 1) {
            logger.info("Database already contains sample data. Skipping initialization.");
            return;
        }
        
        logger.info("Initializing database with sample employee data...");
        
        List<Employee> employees = Arrays.asList(
            // Front Desk Department
            createEmployee("Kasun", "Perera", "kasun.perera@hotel.com", "0771234567",
                "Front Desk Manager", "FRONT_DESK", 85000.0, 
                LocalDate.of(2022, 1, 15), "ACTIVE", "123 Galle Road, Colombo 03",
                "kasun", "MANAGER"),
            
            createEmployee("Sanduni", "Fernando", "sanduni.fernando@hotel.com", "0772345678",
                "Receptionist", "FRONT_DESK", 55000.0, 
                LocalDate.of(2023, 3, 20), "ACTIVE", "456 Bauddhaloka Mawatha, Colombo 07",
                "sanduni", "EMPLOYEE"),
            
            createEmployee("Chaminda", "Silva", "chaminda.silva@hotel.com", "0773456789",
                "Receptionist", "FRONT_DESK", 52000.0, 
                LocalDate.of(2023, 6, 10), "ACTIVE", "789 Duplication Road, Colombo 04",
                "chaminda", "EMPLOYEE"),
            
            // Housekeeping Department
            createEmployee("Nadeeka", "Jayawardena", "nadeeka.jayawardena@hotel.com", "0774567890",
                "Housekeeping Manager", "HOUSEKEEPING", 72000.0, 
                LocalDate.of(2021, 5, 1), "ACTIVE", "321 Baseline Road, Colombo 09",
                "nadeeka", "MANAGER"),
            
            createEmployee("Priyantha", "Dissanayake", "priyantha.dissanayake@hotel.com", "0775678901",
                "Housekeeper", "HOUSEKEEPING", 48000.0, 
                LocalDate.of(2023, 2, 14), "ACTIVE", "654 Kandy Road, Kadawatha",
                "priyantha", "EMPLOYEE"),
            
            createEmployee("Kumari", "Rajapaksha", "kumari.rajapaksha@hotel.com", "0776789012",
                "Housekeeper", "HOUSEKEEPING", 46000.0, 
                LocalDate.of(2023, 7, 22), "ON_LEAVE", "987 Negombo Road, Wattala",
                "kumari", "EMPLOYEE"),
            
            // Kitchen Department
            createEmployee("Roshan", "Wickramasinghe", "roshan.wickramasinghe@hotel.com", "0777890123",
                "Head Chef", "KITCHEN", 95000.0, 
                LocalDate.of(2020, 9, 1), "ACTIVE", "147 Hospital Road, Kalubowila",
                "roshan", "SUPERVISOR"),
            
            createEmployee("Dilini", "Gunawardena", "dilini.gunawardena@hotel.com", "0778901234",
                "Sous Chef", "KITCHEN", 78000.0, 
                LocalDate.of(2021, 11, 15), "ACTIVE", "258 Old Moor Street, Colombo 12",
                "dilini", "SUPERVISOR"),
            
            createEmployee("Nuwan", "Amarasinghe", "nuwan.amarasinghe@hotel.com", "0779012345",
                "Line Cook", "KITCHEN", 58000.0, 
                LocalDate.of(2022, 4, 10), "ACTIVE", "369 High Level Road, Nugegoda",
                "nuwan", "EMPLOYEE"),
            
            createEmployee("Thilini", "Bandara", "thilini.bandara@hotel.com", "0770123456",
                "Pastry Chef", "KITCHEN", 68000.0, 
                LocalDate.of(2022, 8, 5), "ACTIVE", "741 Kotte Road, Rajagiriya",
                "thilini", "EMPLOYEE"),
            
            // Restaurant Department
            createEmployee("Anil", "Samaraweera", "anil.samaraweera@hotel.com", "0711234567",
                "Restaurant Manager", "RESTAURANT", 88000.0, 
                LocalDate.of(2021, 3, 1), "ACTIVE", "852 Kirimandala Mawatha, Narahenpita",
                "anil", "MANAGER"),
            
            createEmployee("Hashini", "De Silva", "hashini.desilva@hotel.com", "0712345678",
                "Waiter", "RESTAURANT", 45000.0, 
                LocalDate.of(2023, 1, 20), "ACTIVE", "963 Nawala Road, Nugegoda",
                "hashini", "EMPLOYEE"),
            
            createEmployee("Buddhika", "Rathnayake", "buddhika.rathnayake@hotel.com", "0713456789",
                "Waiter", "RESTAURANT", 44000.0, 
                LocalDate.of(2023, 5, 15), "ACTIVE", "159 Kadawatha Road, Ragama",
                "buddhika", "EMPLOYEE"),
            
            // Maintenance Department
            createEmployee("Janaka", "Wijesinghe", "janaka.wijesinghe@hotel.com", "0714567890",
                "Maintenance Manager", "MAINTENANCE", 78000.0, 
                LocalDate.of(2020, 7, 1), "ACTIVE", "357 Pannipitiya Road, Battaramulla",
                "janaka", "MANAGER"),
            
            createEmployee("Chamila", "Senarath", "chamila.senarath@hotel.com", "0715678901",
                "Maintenance Technician", "MAINTENANCE", 60000.0, 
                LocalDate.of(2022, 10, 20), "ACTIVE", "486 Homagama Road, Kottawa",
                "chamila", "EMPLOYEE"),
            
            // Management
            createEmployee("Aruna", "Wickremesinghe", "aruna.wickremesinghe@hotel.com", "0716789012",
                "General Manager", "MANAGEMENT", 125000.0, 
                LocalDate.of(2019, 1, 1), "ACTIVE", "579 Green Path, Colombo 03",
                "aruna", "ADMIN"),
            
            createEmployee("Sachini", "Kumarasinghe", "sachini.kumarasinghe@hotel.com", "0717890123",
                "HR Manager", "MANAGEMENT", 92000.0, 
                LocalDate.of(2020, 3, 15), "ACTIVE", "681 Havelock Road, Colombo 05",
                "sachini", "MANAGER"),
            
            createEmployee("Mahinda", "Jayasuriya", "mahinda.jayasuriya@hotel.com", "0718901234",
                "Finance Manager", "MANAGEMENT", 102000.0, 
                LocalDate.of(2020, 6, 1), "ACTIVE", "792 Union Place, Colombo 02",
                "mahinda", "MANAGER"),
            
            // Security Department
            createEmployee("Saman", "Ranasinghe", "saman.ranasinghe@hotel.com", "0719012345",
                "Security Manager", "SECURITY", 75000.0, 
                LocalDate.of(2021, 2, 1), "ACTIVE", "804 Galle Road, Dehiwala",
                "saman", "MANAGER"),
            
            createEmployee("Lakmal", "Gunasekara", "lakmal.gunasekara@hotel.com", "0710123456",
                "Security Guard", "SECURITY", 54000.0, 
                LocalDate.of(2022, 12, 10), "ACTIVE", "915 Moratuwa Road, Piliyandala",
                "lakmal", "EMPLOYEE")
        );
        
        employeeRepository.saveAll(employees);
        logger.info("Successfully initialized database with {} employees", employees.size());
        logger.info("=====================================\n" +
                    "Admin Login Credentials:\n" +
                    "  Username: admin\n" +
                    "  Password: admin123\n" +
                    "  Email: admin@hotel.com\n" +
                    "=====================================\n" +
                    "Default password for all other employees: password123");
    }
    
    private Employee createEmployee(String firstName, String lastName, String email, String phone,
                                     String position, String department, Double salary, 
                                     LocalDate hireDate, String status, String address,
                                     String username, String role) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setHireDate(hireDate);
        employee.setStatus(status);
        employee.setAddress(address);
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode("password123"));
        employee.setRole(role);
        employee.setAccountLocked(false);
        employee.setLoginAttempts(0);
        return employee;
    }
    
    private Employee createEmployeeWithPassword(String firstName, String lastName, String email, String phone,
                                     String position, String department, Double salary, 
                                     LocalDate hireDate, String status, String address,
                                     String username, String role, String password) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setHireDate(hireDate);
        employee.setStatus(status);
        employee.setAddress(address);
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setRole(role);
        employee.setAccountLocked(false);
        employee.setLoginAttempts(0);
        return employee;
    }
}
