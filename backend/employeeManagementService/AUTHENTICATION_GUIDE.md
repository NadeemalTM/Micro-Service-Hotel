# Authentication System Implementation Guide

## Overview
A complete centralized authentication system has been implemented for the Employee Management Service. This service now acts as the authentication server for the entire hotel management microservices ecosystem.

## ✅ Backend Implementation (Completed)

### 1. Dependencies Added
- Spring Security
- JWT Libraries (jjwt-api, jjwt-impl, jjwt-jackson)

### 2. Database Changes
The `Employee` entity now includes:
- `username` - Unique username for login
- `password` - BCrypt hashed password
- `role` - User role (ADMIN, MANAGER, SUPERVISOR, EMPLOYEE)
- `accountLocked` - Boolean flag for locked accounts
- `loginAttempts` - Counter for failed login attempts
- `lastLogin` - Timestamp of last successful login

### 3. New Components Created

#### Authentication DTOs
- `LoginRequest` - Username and password input
- `LoginResponse` - JWT token and user details
- `AuthValidateRequest` - Token validation request
- `AuthValidateResponse` - Token validation result
- `ChangePasswordRequest` - Password change request

#### Security Services
- `JwtService` - JWT token generation and validation
- `CustomUserDetailsService` - Load user details from database
- `AuthenticationService` - Main authentication logic
- `JwtAuthenticationFilter` - Intercept and validate JWT tokens
- `SecurityConfig` - Spring Security configuration

#### Authentication Controller
New endpoints at `/api/auth`:
- `POST /api/auth/login` - User login
- `POST /api/auth/validate` - Validate JWT token (for other microservices)
- `POST /api/auth/change-password` - Change password
- `POST /api/auth/unlock-account/{id}` - Unlock locked account
- `GET /api/auth/me` - Get current user info

### 4. Security Features
✅ BCrypt password hashing
✅ JWT token with 24-hour expiration
✅ Account lockout after 5 failed attempts
✅ CORS enabled for frontend origins
✅ Protected endpoints (all API calls require authentication except /api/auth/*)
✅ Role-based access control (RBAC)

### 5. Default Credentials
All 20 employees have been initialized with credentials:

**Admin Account:**
- Username: `admin`
- Password: `password123`
- Role: ADMIN
- Name: Aruna Wickremesinghe

**Sample Accounts:**
- Username: `kasun` | Role: MANAGER | Password: `password123`
- Username: `sanduni` | Role: EMPLOYEE | Password: `password123`
- Username: `roshan` | Role: SUPERVISOR | Password: `password123`

**All employees use password:** `password123`

## ✅ Frontend Implementation (Completed)

### 1. New Components Created
- `AuthContext.jsx` - Authentication state management
- `ProtectedRoute.jsx` - Route guard component
- `Login.jsx` - Login page with form
- `Login.css` - Login page styling

### 2. Features Implemented
✅ Login page with username/password form
✅ JWT token storage in localStorage
✅ Automatic token injection in API requests
✅ Protected routes (redirect to login if not authenticated)
✅ User info display in sidebar
✅ Logout functionality
✅ Auto-redirect on 401 errors
✅ Loading states

### 3. Updated Components
- `App.jsx` - Added AuthProvider and protected routes
- `Sidebar.jsx` - Added user info and logout button
- `Sidebar.css` - Styled user info section
- `api.js` - Added request/response interceptors

## 🔐 Security Flow

### Login Process
1. User enters username and password
2. Frontend sends credentials to `/api/auth/login`
3. Backend validates credentials
4. If valid, generates JWT token
5. Token and user info returned to frontend
6. Frontend stores token in localStorage
7. User redirected to dashboard

### Authenticated Requests
1. Frontend attaches JWT token to every API request
2. Backend JWT filter intercepts request
3. Token validated and user authenticated
4. Request proceeds if valid
5. 401 error if invalid/expired

### Account Lockout
- After 5 failed login attempts, account is locked
- Admin can unlock using `/api/auth/unlock-account/{id}`

## 🔌 Integration with Other Microservices

### For Other Services to Use This Authentication

1. **Add HTTP Client dependency** to call Employee Management Service

2. **Create Authentication Filter/Interceptor:**
```java
// Extract token from Authorization header
String token = request.getHeader("Authorization");

// Call Employee Management Service to validate
POST http://localhost:8085/api/auth/validate
Body: { "token": "Bearer eyJhbGc..." }

// Response contains user info if valid
{
  "success": true,
  "data": {
    "valid": true,
    "username": "admin",
    "role": "ADMIN",
    "employeeId": 1
  }
}
```

3. **Configure Service:**
```yaml
auth:
  service:
    url: http://localhost:8085/api/auth
```

4. **Protect Endpoints:**
- All endpoints require valid JWT token
- Extract user info from validation response
- Apply role-based access control

## 📝 API Documentation

### Login
```bash
POST http://localhost:8085/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}

Response:
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

### Validate Token
```bash
POST http://localhost:8085/api/auth/validate
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

Response:
{
  "success": true,
  "data": {
    "valid": true,
    "username": "admin",
    "email": "aruna.wickremesinghe@hotel.com",
    "role": "ADMIN",
    "employeeId": 16,
    "message": "Token is valid"
  }
}
```

### Protected Endpoint Example
```bash
GET http://localhost:8085/api/employees
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 🧪 Testing

### Test Login
1. Start backend: `cd backend/employeeManagementService && ./mvnw spring-boot:run`
2. Start frontend: `cd frontend/employee-management && npm run dev`
3. Visit: http://localhost:5173/login
4. Login with username: `admin`, password: `password123`

### Test Protected Routes
1. Try accessing http://localhost:5173/ without logging in → Redirects to /login
2. After login → Can access all pages

### Test Account Lockout
1. Try logging in with wrong password 5 times
2. Account will be locked
3. Unlock via API: `POST /api/auth/unlock-account/1`

## 🔧 Configuration

### JWT Settings (application.properties)
```properties
# JWT Secret Key (Base64 encoded)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# JWT Expiration (24 hours in milliseconds)
jwt.expiration=86400000
```

### CORS Configuration
Allowed origins: `http://localhost:5173`, `http://localhost:3000`

## 📦 Files Created/Modified

### Backend (New Files)
1. `JwtService.java`
2. `CustomUserDetailsService.java`
3. `AuthenticationService.java`
4. `JwtAuthenticationFilter.java`
5. `SecurityConfig.java`
6. `AuthenticationController.java`
7. `LoginRequest.java`
8. `LoginResponse.java`
9. `AuthValidateRequest.java`
10. `AuthValidateResponse.java`
11. `ChangePasswordRequest.java`

### Backend (Modified Files)
1. `pom.xml` - Added dependencies
2. `Employee.java` - Added auth fields
3. `EmployeeRepository.java` - Added findByUsername
4. `DataInitializer.java` - Added credentials for all employees

### Frontend (New Files)
1. `AuthContext.jsx`
2. `ProtectedRoute.jsx`
3. `Login.jsx`
4. `Login.css`

### Frontend (Modified Files)
1. `App.jsx` - Added AuthProvider and protected routes
2. `Sidebar.jsx` - Added user info and logout
3. `Sidebar.css` - Added user info styling
4. `api.js` - Added JWT interceptors

## ⚠️ Important Notes

1. **Database Reset:** If you need to reset the database, delete the H2 database file and restart the backend. All employees will be recreated with default password `password123`.

2. **Token Expiration:** JWT tokens expire after 24 hours. Users will need to login again.

3. **Password Security:** In production, ensure:
   - Change the JWT secret key
   - Use HTTPS only
   - Implement password complexity requirements
   - Force password change on first login

4. **Admin Account:** The admin account (username: `admin`) has full access. Protect it carefully.

5. **Other Services:** When integrating other microservices, they should call the `/api/auth/validate` endpoint to verify tokens rather than implementing their own authentication logic.

## 🎉 Success!

Your centralized authentication system is now fully functional. All employee management service endpoints are protected, and you have a working login system for your hotel management application.

**Default Admin Login:**
- URL: http://localhost:5173/login
- Username: `admin`
- Password: `password123`
