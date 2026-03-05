# Employee Management Service

Employee Management Service for Hotel Management System - A comprehensive microservice for managing employee information, departments, positions, status tracking, and **centralized authentication** for all microservices.

## 🔐 Authentication System

This service acts as the **Authentication Server** for the entire hotel management microservices ecosystem.

### Key Features
- ✅ **JWT Token-based Authentication** - Secure token generation and validation
- ✅ **BCrypt Password Encryption** - Industry-standard password hashing
- ✅ **Role-Based Access Control (RBAC)** - ADMIN, MANAGER, SUPERVISOR, EMPLOYEE roles
- ✅ **Account Security** - Automatic lockout after 5 failed login attempts
- ✅ **Token Validation API** - Other microservices can validate tokens
- ✅ **Session Management** - 24-hour token expiration
- ✅ **Protected Endpoints** - All API calls require authentication

### Default Admin Credentials
```
Username: admin
Password: password123
Role: ADMIN
```

### Sample User Accounts
- `kasun` / `password123` (MANAGER - Front Desk)
- `sanduni` / `password123` (EMPLOYEE - Front Desk)
- `roshan` / `password123` (SUPERVISOR - Kitchen)
- `anil` / `password123` (MANAGER - Restaurant)

**All 20 employees have default password:** `password123`

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/auth/login` | User login (returns JWT token) | ❌ No |
| `POST` | `/api/auth/validate` | Validate JWT token | ❌ No |
| `POST` | `/api/auth/change-password` | Change password | ✅ Yes |
| `POST` | `/api/auth/unlock-account/{id}` | Unlock locked account | ✅ Yes |
| `GET` | `/api/auth/me` | Get current user info | ✅ Yes |

### Login Example
```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "email": "aruna.wickremesinghe@hotel.com",
    "fullName": "Aruna Wickremesinghe",
    "role": "ADMIN",
    "employeeId": 16
  }
}
```

### Using JWT Token in Requests
```bash
curl -X GET http://localhost:8085/api/employees \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

📖 **Detailed Authentication Guide:** See [AUTHENTICATION_GUIDE.md](AUTHENTICATION_GUIDE.md) for complete implementation details.

## 📌 Quick Start

The Employee Management Service with Authentication:

- **Service URL**: http://localhost:8085
- **Frontend URL**: http://localhost:5173 (React + Vite)
- **Login Page**: http://localhost:5173/login
- **API Documentation (Swagger)**: http://localhost:8085/swagger-ui.html
- **Health Check**: http://localhost:8085/actuator/health
- **H2 Database Console**: http://localhost:8085/h2-console

**Pre-loaded Data**: 20 Sri Lankan employees with credentials automatically loaded on startup.

## 🚀 Features

### Authentication & Security
- **JWT Token Authentication**: Secure token-based authentication system
- **Password Encryption**: BCrypt hashing with salt
- **Role-Based Access Control**: ADMIN, MANAGER, SUPERVISOR, EMPLOYEE roles
- **Account Lockout**: Automatic lockout after 5 failed login attempts
- **Token Validation API**: For other microservices to validate authentication
- **Protected Endpoints**: All employee data APIs require valid JWT token

### Employee Management
- **Complete CRUD Operations**: Create, Read, Update, and Delete employee records
- **Advanced Search & Filtering**: 
  - Search employees by name (first name or last name)
  - Filter by department, position, and status
  - Retrieve employees by email
- **Employee Status Management**: Track employee status (ACTIVE, INACTIVE, ON_LEAVE)
- **Department Organization**: Manage employees across multiple departments (FRONT_DESK, HOUSEKEEPING, KITCHEN, RESTAURANT, MAINTENANCE, MANAGEMENT, SECURITY)
- **Statistics & Analytics**:
  - Overall employee statistics (total, active, inactive, on leave)
  - Department-wise statistics (employee count, average salary, total salary expense)
  - Employee count and active employee count endpoints

### Technical Features
- **Input Validation**: Comprehensive validation on all employee data
- **Exception Handling**: Global exception handling with detailed error responses
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Logging**: Detailed logging for monitoring and debugging
- **Health Monitoring**: Spring Boot Actuator endpoints for health checks
- **CORS Enabled**: Cross-origin requests allowed for frontend integration
- **Sample Data**: Pre-loaded with 20 Sri Lankan employees with credentials for testing

## 🛠️ Technology Stack

### Core Framework
- **Java 21**
- **Spring Boot 3.2.1**
- **Spring Data JPA** - Database access and ORM
- **Spring Boot Validation** - Input validation

### Security
- **Spring Security** - Authentication and authorization framework
- **JWT (JSON Web Tokens)** - Token-based authentication
- **BCrypt** - Password hashing algorithm

### Database & Storage
- **H2 Database** - In-memory database for development
- **Hibernate** - ORM implementation

### Documentation & Monitoring
- **Swagger/OpenAPI 3** - Interactive API documentation
- **Spring Boot Actuator** - Health checks and monitoring
- **SLF4J/Logback** - Application logging

### Testing & Build
- **JUnit 5 & Mockito** - Unit testing framework
- **Maven** - Build and dependency management

## 📋 Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.8+ (or use included Maven Wrapper)
- Any IDE (IntelliJ IDEA, Eclipse, VS Code recommended)

## 🏃 Running the Service

### Backend (Spring Boot)

#### Using Maven Wrapper (Recommended)
```bash
# Navigate to backend directory
cd backend/employeeManagementService

# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Service will start on: **http://localhost:8085**

#### Using Maven
```bash
mvn spring-boot:run
```

#### Using JAR
```bash
mvn clean package
java -jar target/employeeManagementService-0.0.1-SNAPSHOT.jar
```

#### Using Docker
```bash
# Build Docker image
docker build -t employee-management-service .

# Run Docker container
docker run -p 8085:8085 employee-management-service
```

### Frontend (React + Vite)

```bash
# Navigate to frontend directory
cd frontend/employee-management

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

Frontend will start on: **http://localhost:5173**

### Access Points
- **Login Page**: http://localhost:5173/login
- **Dashboard**: http://localhost:5173/ (requires login)
- **API Swagger**: http://localhost:8085/swagger-ui.html
- **H2 Console**: http://localhost:8085/h2-console

## 🎯 Testing the Service

### Option 1: Using the Frontend (Recommended)
1. Start both backend and frontend services
2. Open your browser: **http://localhost:5173/login**
3. Login with:
   - Username: `admin`
   - Password: `password123`
4. Navigate through the dashboard to:
   - View all employees
   - Add new employees
   - Edit employee details
   - View statistics
   - Search and filter employees

### Option 2: Using Swagger UI
1. First, **login to get a JWT token**:
   ```bash
   curl -X POST http://localhost:8085/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password123"}'
   ```
   
2. Copy the token from the response

3. Open Swagger UI: **http://localhost:8085/swagger-ui.html**

4. Click "Authorize" button at the top

5. Enter: `Bearer <your-token>` (replace `<your-token>` with actual token)

6. Now you can test all endpoints interactively

### Option 3: Using cURL Commands

**Step 1: Login and get token**
```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

**Step 2: Use token in requests**

**Get All Employees:**
```bash
curl -X GET http://localhost:8085/api/employees \
  -H "Authorization: Bearer <your-token>"
```

**Get Employee by ID:**
```bash
curl -X GET http://localhost:8085/api/employees/1
```

**Get Employee Statistics:**
```bash
curl -X GET http://localhost:8085/api/employees/statistics
```

**Get Department Statistics:**
```bash
curl -X GET http://localhost:8085/api/employees/statistics/departments
```

**Search Employees by Name:**
```bash
curl -X GET "http://localhost:8085/api/employees/search?name=John"
```

**Filter by Department:**
```bash
curl -X GET http://localhost:8085/api/employees/department/KITCHEN
```

**Filter by Status:**
```bash
curl -X GET http://localhost:8085/api/employees/status/ACTIVE
```

### Option 3: Using Postman or Thunder Client
1. Import the OpenAPI specification from: http://localhost:8085/api-docs
2. Or manually create requests using the endpoints listed below

## 📡 API Endpoints

### Authentication Endpoints (Public - No Token Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/login` | User login (returns JWT token) |
| `POST` | `/api/auth/validate` | Validate JWT token (for microservices) |

### Authentication Endpoints (Protected - Token Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/change-password` | Change account password |
| `POST` | `/api/auth/unlock-account/{id}` | Unlock locked account (Admin) |
| `GET` | `/api/auth/me` | Get current authenticated user |

### Employee Endpoints (Protected - Token Required)

**Base URL:** `http://localhost:8085/api/employees`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/employees` | Create a new employee |
| `GET` | `/api/employees` | Get all employees |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `GET` | `/api/employees/email/{email}` | Get employee by email |
| `GET` | `/api/employees/department/{department}` | Get employees by department |
| `GET` | `/api/employees/status/{status}` | Get employees by status |
| `GET` | `/api/employees/position/{position}` | Get employees by position |
| `GET` | `/api/employees/search?name={name}` | Search employees by name |
| `PUT` | `/api/employees/{id}` | Update employee |
| `PATCH` | `/api/employees/{id}/status?status={status}` | Update employee status |
| `DELETE` | `/api/employees/{id}` | Delete employee |
| `GET` | `/api/employees/statistics` | Get overall employee statistics |
| `GET` | `/api/employees/statistics/departments` | Get department-wise statistics |
| `GET` | `/api/employees/count` | Get total employee count |
| `GET` | `/api/employees/count/active` | Get active employee count |

⚠️ **Important:** All employee endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

### Example Request: Create Employee

```bash
curl -X POST http://localhost:8085/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@hotel.com",
    "phone": "1234567890",
    "position": "Front Desk Manager",
    "department": "FRONT_DESK",
    "salary": 50000,
    "hireDate": "2024-01-15",
    "status": "ACTIVE",
    "address": "123 Main Street, City"
  }'
```

### Example Response
```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@hotel.com",
    "phone": "1234567890",
    "position": "Front Desk Manager",
    "department": "FRONT_DESK",
    "salary": 50000.0,
    "hireDate": "2024-01-15",
    "status": "ACTIVE",
    "address": "123 Main Street, City"
  },
  "timestamp": "2024-02-18T10:30:00"
}
```

## 📊 Employee Departments

- `FRONT_DESK` - Front desk and reception
- `HOUSEKEEPING` - Housekeeping and cleaning
- `KITCHEN` - Kitchen and culinary
- `RESTAURANT` - Restaurant and dining
- `MAINTENANCE` - Maintenance and repairs
- `MANAGEMENT` - Management and administration
- `SECURITY` - Security personnel
- `EVENTS` - Event management
- `HR` - Human resources
- `FINANCE` - Finance and accounting
- `IT` - Information technology

## 🔍 Employee Status Values

- `ACTIVE` - Currently employed and working
- `INACTIVE` - Not currently active
- `ON_LEAVE` - On leave (sick, vacation, etc.)

### Example: Get Employee Statistics

```bash
curl -X GET http://localhost:8085/api/employees/statistics
```

**Response:**
```json
{
  "success": true,
  "message": "Statistics retrieved successfully",
  "data": {
    "totalEmployees": 20,
    "activeEmployees": 17,
    "inactiveEmployees": 1,
    "onLeaveEmployees": 1,
    "averageSalary": 47150.0,
    "totalSalaryExpense": 943000.0
  },
  "timestamp": "2024-02-18T10:30:00"
}
```

### Example: Get Department Statistics

```bash
curl -X GET http://localhost:8085/api/employees/statistics/departments
```

**Response:**
```json
{
  "success": true,
  "message": "Department statistics retrieved successfully",
  "data": [
    {
      "department": "FRONT_DESK",
      "employeeCount": 3,
      "averageSalary": 41333.33,
      "totalSalaryExpense": 124000.0,
      "activeCount": 3
    },
    {
      "department": "KITCHEN",
      "employeeCount": 4,
      "averageSalary": 50000.0,
      "totalSalaryExpense": 200000.0,
      "activeCount": 4
    }
  ],
  "timestamp": "2024-02-18T10:30:00"
}
```

## 📚 API Documentation

### Swagger UI
Access the interactive API documentation:
```
http://localhost:8085/swagger-ui.html
```

### OpenAPI Specification
Access the raw OpenAPI specification:
```
http://localhost:8085/api-docs
```

## 🗄️ Database Configuration

### H2 Console
Access the H2 database console for debugging:
```
URL: http://localhost:8085/h2-console
JDBC URL: jdbc:h2:mem:employeedb
Username: sa
Password: (leave blank)
```

## 🏥 Health & Monitoring

### Actuator Endpoints
- **Health Check**: `http://localhost:8085/actuator/health`
- **Info**: `http://localhost:8085/actuator/info`
- **Metrics**: `http://localhost:8085/actuator/metrics`

## 🧪 Testing

### Run Unit Tests
```bash
# Windows
mvnw.cmd test

# Linux/Mac
./mvnw test
```

**Test Results:**
- ✅ **28 tests passing**
- 11 Controller tests
- 16 Service tests  
- 1 Application context test

### Run Tests with Coverage
```bash
mvnw.cmd clean test jacoco:report
```

### Test Classes
- `EmployeeControllerTest` - REST controller unit tests (11 tests)
- `EmployeeServiceTest` - Business logic unit tests (16 tests)
- `EmployeeManagementServiceApplicationTests` - Application context test (1 test)

## 📊 Sample Data

The service automatically loads **20 sample employees** on startup across 7 departments:

| Department | Employees | Positions |
|------------|-----------|-----------|
| **FRONT_DESK** | 3 | Manager, Receptionist (2) |
| **HOUSEKEEPING** | 3 | Manager, Housekeeper (2) |
| **KITCHEN** | 4 | Head Chef, Sous Chef, Line Cook, Pastry Chef |
| **RESTAURANT** | 3 | Manager, Waiter (2) |
| **MAINTENANCE** | 2 | Manager, Technician |
| **MANAGEMENT** | 3 | General Manager, HR Manager, Finance Manager |
| **SECURITY** | 2 | Manager, Security Guard |

**Status Distribution:**
- Active: 19 employees
- Inactive: 0 employees
- On Leave: 1 employee

**Salary Range:** Rs. 44,000 - Rs. 125,000
**Average Salary:** Rs. 72,550

**All employees have login credentials:**
- Username: Based on first name (lowercase)
- Password: `password123` (default for all)
- Roles: Based on position (ADMIN, MANAGER, SUPERVISOR, EMPLOYEE)

## 🎨 Frontend Application

The service includes a complete **React + Vite** frontend application with modern UI.

### Features
- **Authentication System**: Login page with JWT token management
- **Protected Routes**: All pages require authentication
- **Dashboard**: Overview statistics and quick actions
- **Employee List**: Search, filter, and manage employees
- **Employee Details**: View complete employee information
- **Add/Edit Forms**: Create and update employee records
- **Statistics Page**: Department-wise analytics and insights
- **User Profile**: Display logged-in user info with logout
- **Responsive Design**: Modern blue gradient theme with gold accents
- **Hotel Branding**: ආලකමන්දා (Alakamanda) Hotel logo

### Technology Stack
- **React 18.3.1** - UI library
- **Vite 8.0.0** - Build tool with HMR
- **React Router DOM v7** - Client-side routing
- **TanStack React Query v5** - Data fetching and caching
- **Axios** - HTTP client with JWT interceptors
- **Lucide React** - Icon library

### Pages
1. **Login** (`/login`) - Authentication page
2. **Dashboard** (`/`) - Statistics overview
3. **Employee List** (`/employees`) - Table with search/filter
4. **Employee Details** (`/employees/:id`) - Individual profile
5. **Add Employee** (`/employees/add`) - Creation form
6. **Edit Employee** (`/employees/edit/:id`) - Update form
7. **Statistics** (`/statistics`) - Department analytics

### Running Frontend
```bash
cd ../../frontend/employee-management
npm install       # First time only
npm run dev       # Start dev server
```

Access at: **http://localhost:5173**

## 🔧 Configuration

### Application Properties (`application.yaml`)

```yaml
server:
  port: 8085

spring:
  application:
    name: employeeManagementService
  datasource:
    url: jdbc:h2:mem:employeedb
  jpa:
    hibernate:
      ddl-auto: update
```

## 📦 Project Structure

### Backend (Spring Boot)
```
employeeManagementService/
├── src/
│   ├── main/
│   │   ├── java/com/nsbm/group03/employeeManagementService/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java          # Loads 20 employees with credentials
│   │   │   │   ├── OpenAPIConfig.java            # Swagger configuration
│   │   │   │   ├── SecurityConfig.java           # Spring Security + JWT config
│   │   │   │   └── JwtAuthenticationFilter.java  # JWT token validation filter
│   │   │   ├── controller/
│   │   │   │   ├── EmployeeController.java       # 15 employee REST endpoints
│   │   │   │   └── AuthenticationController.java # 5 auth endpoints
│   │   │   ├── dto/
│   │   │   │   ├── EmployeeDTO.java              # Employee data transfer object
│   │   │   │   ├── EmployeeStatisticsDTO.java    # Statistics DTO
│   │   │   │   ├── DepartmentStatisticsDTO.java  # Dept statistics DTO
│   │   │   │   ├── LoginRequest.java             # Login credentials
│   │   │   │   ├── LoginResponse.java            # JWT token + user info
│   │   │   │   ├── AuthValidateRequest.java      # Token validation request
│   │   │   │   ├── AuthValidateResponse.java     # Token validation response
│   │   │   │   └── ChangePasswordRequest.java    # Password change request
│   │   │   ├── entity/
│   │   │   │   └── Employee.java                 # JPA entity with auth fields
│   │   │   ├── enums/
│   │   │   │   ├── Department.java               # Department enum
│   │   │   │   └── EmployeeStatus.java           # Status enum
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java   # Centralized exception handling
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   └── ErrorResponse.java            # Error response structure
│   │   │   ├── repository/
│   │   │   │   └── EmployeeRepository.java       # JPA repository with auth queries
│   │   │   ├── response/
│   │   │   │   └── ApiResponse.java              # Standard API response wrapper
│   │   │   ├── service/
│   │   │   │   ├── EmployeeService.java          # Business logic (15+ methods)
│   │   │   │   ├── AuthenticationService.java    # Login, token validation, lockout
│   │   │   │   ├── JwtService.java               # JWT token generation/validation
│   │   │   │   └── CustomUserDetailsService.java # Spring Security user loading
│   │   │   └── EmployeeManagementServiceApplication.java # Main application
│   │   └── resources/
│   │       ├── application.yaml              # Configuration + JWT settings
│   │       └── application.properties
│   └── test/
│       └── java/com/nsbm/group03/employeeManagementService/
│           ├── controller/
│           │   └── EmployeeControllerTest.java # 11 controller tests
│           ├── service/
│           │   └── EmployeeServiceTest.java    # 16 service tests
│           └── EmployeeManagementServiceApplicationTests.java # 1 context test
├── target/                                    # Build output
├── Dockerfile                                 # Docker configuration
├── pom.xml                                    # Maven dependencies (Security + JWT)
├── mvnw.cmd / mvnw                           # Maven wrapper
├── README.md                                  # This file
└── AUTHENTICATION_GUIDE.md                    # Complete auth implementation guide
```

### Frontend (React + Vite)
```
employee-management/
├── public/
│   ├── Hotel_Logo.png                        # Hotel branding logo
│   └── vite.svg
├── src/
│   ├── assets/
│   ├── components/
│   │   ├── Layout.jsx                        # Main layout wrapper
│   │   ├── Layout.css
│   │   ├── Sidebar.jsx                       # Navigation with user profile
│   │   ├── Sidebar.css
│   │   └── ProtectedRoute.jsx                # Route guard for authentication
│   ├── context/
│   │   └── AuthContext.jsx                   # Authentication state management
│   ├── pages/
│   │   ├── Login.jsx                         # Login page
│   │   ├── Login.css
│   │   ├── Dashboard.jsx                     # Statistics overview
│   │   ├── Dashboard.css
│   │   ├── EmployeeList.jsx                  # Employee table with search/filter
│   │   ├── EmployeeList.css
│   │   ├── EmployeeDetails.jsx               # Individual employee view
│   │   ├── EmployeeDetails.css
│   │   ├── AddEmployee.jsx                   # Create employee form
│   │   ├── EditEmployee.jsx                  # Update employee form
│   │   ├── EmployeeForm.css
│   │   ├── Statistics.jsx                    # Department analytics
│   │   └── Statistics.css
│   ├── services/
│   │   └── api.js                            # Axios with JWT interceptors
│   ├── App.jsx                               # Root component with routing
│   ├── App.css                               # Global styles
│   ├── main.jsx                              # Entry point
│   └── index.css
├── package.json                              # Dependencies
├── vite.config.js                            # Vite configuration
├── IMPLEMENTATION_SUMMARY.md                 # Frontend implementation details
└── README.md                                 # Frontend documentation
```

**Total Backend Files:**
- **28 Java source files** (11 new auth files)
- **3 Test files** with 28 passing tests
- **2 Configuration files** (YAML & Properties)

**Total Frontend Files:**
- **22 React components/pages**
- **15 CSS files**
- **1 API service with JWT interceptors**
└── README.md                                  # This file
```

**Total Files:**
- **17 Java source files** in `src/main/java`
- **3 Test files** with 28 passing tests
- **2 Configuration files** (YAML & Properties)

## 🔐 Validation Rules

### Employee DTO Validation
- **First Name**: Required, 2-50 characters
- **Last Name**: Required, 2-50 characters
- **Email**: Required, valid email format, unique
- **Phone**: Required, 10-15 digits
- **Position**: Required, 2-100 characters
- **Department**: Required
- **Salary**: Required, must be greater than 0
- **Hire Date**: Required, cannot be in the future
- **Status**: Required
- **Address**: Optional, max 500 characters

## 🚨 Error Handling

The service provides detailed error responses:

```json
{
  "timestamp": "2024-02-18T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 1",
  "path": "/api/employees/1"
}
```

### Common HTTP Status Codes
- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource (e.g., email already exists)
- `500 Internal Server Error` - Server error

## 🤝 Integration with Other Services

This service is designed to integrate with other microservices in the Hotel Management System:
- **Authentication Service** - Employee authentication
- **Reservation Service** - Employee assignments
- **Event Management Service** - Staff allocation for events
- **Kitchen Management Service** - Kitchen staff management

## 👥 Authors

- **NSBM Group 03**
- Contact: support@hotelmanagement.com

## 📝 License

This project is licensed under the MIT License.

## 🔄 Version History

- **v1.0.0** (February 18, 2026) - Initial release
  - ✅ Complete CRUD operations for employee management
  - ✅ Advanced search and filtering (by name, department, position, status, email)
  - ✅ Employee status management (ACTIVE, INACTIVE, ON_LEAVE)
  - ✅ Statistics endpoints (overall & department-wise)
  - ✅ Input validation with comprehensive error messages
  - ✅ Global exception handling
  - ✅ Swagger/OpenAPI documentation
  - ✅ 28 passing unit tests (100% test coverage for business logic)
  - ✅ Pre-loaded sample data (20 employees across 7 departments)
  - ✅ H2 in-memory database for development
  - ✅ Spring Boot Actuator for health monitoring
  - ✅ Logging with SLF4J
  - ✅ Docker support

## 🎓 Key Features Summary

### ✨ What Makes This Service Complete:

1. **Production-Ready Code**
   - Proper layered architecture (Controller → Service → Repository)
   - DTOs for data transfer
   - Entity classes for database mapping
   - Clean separation of concerns

2. **Comprehensive API (15 Endpoints)**
   - CRUD operations (Create, Read, Update, Delete)
   - Advanced search and filtering
   - Status management
   - Statistics and analytics

3. **Data Validation**
   - Email format and uniqueness validation
   - Phone number format validation
   - Required field validation
   - Date validation (hire date cannot be in future)
   - Salary validation (must be > 0)

4. **Error Handling**
   - Global exception handler
   - Custom exceptions (ResourceNotFound, DuplicateResource)
   - Detailed error responses with timestamps
   - Proper HTTP status codes

5. **Testing**
   - 28 unit tests covering all major functionality
   - MockMvc for controller testing
   - Mockito for service testing
   - All tests passing ✅

6. **Documentation**
   - Interactive Swagger UI
   - OpenAPI 3.0 specification
   - Comprehensive README
   - Code comments and logging

7. **Developer Experience**
   - Auto-loaded sample data
   - H2 console for database inspection
   - Hot reload with Spring DevTools
   - Maven wrapper included

## 🚀 Quick Commands Reference

```bash
# Build the project
mvnw.cmd clean compile

# Run tests
mvnw.cmd test

# Run the application
mvnw.cmd spring-boot:run

# Package as JAR
mvnw.cmd clean package

# Build Docker image
docker build -t employee-service .
```

## 🌐 Important URLs

| Service | URL |
|---------|-----|
| **Application** | http://localhost:8085 |
| **Swagger UI** | http://localhost:8085/swagger-ui.html |
| **OpenAPI Docs** | http://localhost:8085/api-docs |
| **H2 Console** | http://localhost:8085/h2-console |
| **Health Check** | http://localhost:8085/actuator/health |
| **Base API** | http://localhost:8085/api/employees |

## 🙏 Acknowledgments

- Spring Boot Framework
- Spring Data JPA
- Swagger/OpenAPI (SpringDoc)
- H2 Database
- Maven Build Tool
- JUnit & Mockito Testing Frameworks

---

## 📞 Support & Contact

For questions or issues related to the Employee Management Service:
- **Project**: Microservice-Based Hotel Management System
- **Team**: NSBM Group 03
- **Service**: Employee Management Microservice
- **Status**: ✅ Running & Tested

**Swagger Documentation**: Always use Swagger UI for the most up-to-date API documentation and interactive testing.

---

*Last Updated: February 18, 2026*

- Spring Boot Documentation
- Spring Data JPA
- Swagger/OpenAPI
- H2 Database


### Using Docker
```bash
docker build -t employee-service .
docker run -p 8085:8085 employee-service
```

## Employee Model
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@hotel.com",
  "phone": "+1234567890",
  "position": "Front Desk Manager",
  "department": "Reception",
  "salary": 45000.00,
  "hireDate": "2024-01-15",
  "status": "ACTIVE",
  "address": "123 Main St, City, Country"
}
```

## Health Check
- http://localhost:8085/actuator/health
