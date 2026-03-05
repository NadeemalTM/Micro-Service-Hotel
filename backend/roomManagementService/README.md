# Room Management Service

Room Management Service for Hotel Management System - A comprehensive microservice for managing hotel rooms, availability, pricing, and maintenance.

## 🚀 Features

- **Complete CRUD Operations**: Create, Read, Update, and Delete room records
- **Room Search & Filtering**: Search by floor, type, status, and availability
- **Status Management**: Track room availability (Available, Occupied, Maintenance, Reserved)
- **Price Management**: Flexible pricing per room type
- **Maintenance Tracking**: Record cleaning and maintenance activities
- **Statistics & Analytics**: Room occupancy rates and revenue insights
- **RESTful API**: Clean and well-documented API endpoints
- **Swagger Documentation**: Interactive API documentation

## 📌 Quick Start

- **Service URL**: http://localhost:8086
- **API Documentation (Swagger)**: http://localhost:8086/swagger-ui.html
- **Health Check**: http://localhost:8086/actuator/health
- **H2 Database Console**: http://localhost:8086/h2-console

**Pre-loaded Data**: 12 sample rooms across 4 floors automatically loaded on startup.

## 🛠️ Tech Stack

- **Spring Boot 3.2.1**: Framework
- **Java 17**: Programming language
- **Spring Data JPA**: Data persistence
- **H2 Database**: In-memory database
- **Swagger/OpenAPI**: API documentation
- **Maven**: Build tool

## 📋 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/rooms` | Get all rooms |
| `GET` | `/api/rooms/{id}` | Get room by ID |
| `GET` | `/api/rooms/number/{roomNumber}` | Get room by number |
| `GET` | `/api/rooms/floor/{floor}` | Get rooms by floor |
| `GET` | `/api/rooms/type/{roomType}` | Get rooms by type |
| `GET` | `/api/rooms/status/{status}` | Get rooms by status |
| `GET` | `/api/rooms/available/{roomType}` | Get available rooms by type |
| `GET` | `/api/rooms/statistics` | Get room statistics |
| `POST` | `/api/rooms` | Create new room |
| `PUT` | `/api/rooms/{id}` | Update room |
| `DELETE` | `/api/rooms/{id}` | Delete room |
| `PATCH` | `/api/rooms/{id}/status?status={STATUS}` | Update room status |
| `PATCH` | `/api/rooms/{id}/clean` | Mark room as cleaned |
| `PATCH` | `/api/rooms/{id}/maintenance` | Record room maintenance |

## 🏨 Room Types

- **STANDARD**: Basic comfortable rooms (₹75/night)
- **DELUXE**: Enhanced rooms with balcony (₹120/night)
- **SUITE**: Spacious suites with living area (₹200/night)
- **EXECUTIVE**: Premium suites with luxury amenities (₹300/night)
- **PRESIDENTIAL**: Top-tier luxury suite (₹500/night)

## 🔧 Installation & Running

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run the service
```bash
# Navigate to service directory
cd backend/roomManagementService

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The service will start on **http://localhost:8086**

## 📊 Sample Data

12 rooms are automatically initialized:
- **Floor 1**: 4 rooms (Standard & Deluxe)
- **Floor 2**: 4 rooms (Deluxe & Suite)
- **Floor 3**: 3 rooms (Suite & Executive)
- **Floor 4**: 1 room (Presidential Suite)

## 🐳 Docker Deployment

```bash
# Build Docker image
docker build -t room-management-service .

# Run container
docker run -p 8086:8086 room-management-service
```

## ☁️ AWS Deployment

This service is configured for AWS deployment using Elastic Beanstalk or EC2.

See `Dockerfile` and `Procfile` for deployment configuration.

## 🔗 Integration

This service integrates with:
- **Employee Management Service** (Port 8085) - For authentication
- **Reservation Management Service** (Port 8087) - For booking management

## 📝 License

NSBM Group 03 - Hotel Management System © 2026
