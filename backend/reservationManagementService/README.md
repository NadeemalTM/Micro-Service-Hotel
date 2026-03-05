# Reservation Management Service

Comprehensive reservation management service for the hotel management system.

## Features

- ✅ Complete reservation lifecycle management
- ✅ Check-in/Check-out tracking
- ✅ Guest information management
- ✅ Payment status tracking
- ✅ Room availability validation
- ✅ Reservation statistics and reporting
- ✅ Multi-guest support (adults/children)
- ✅ Special services (breakfast, parking, airport pickup)
- ✅ Discount and pricing management
- ✅ Cancellation handling with refunds
- ✅ Swagger API documentation
- ✅ H2 in-memory database

## Port
- **8087**

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/reservations` | Get all reservations |
| GET | `/api/reservations/{id}` | Get reservation by ID |
| GET | `/api/reservations/status/{status}` | Get reservations by status |
| GET | `/api/reservations/room/{roomId}` | Get reservations for a room |
| GET | `/api/reservations/guest/{email}` | Get reservations for a guest |
| GET | `/api/reservations/current-guests` | Get currently checked-in guests |
| GET | `/api/reservations/upcoming-checkins` | Get today's check-ins |
| GET | `/api/reservations/upcoming-checkouts` | Get today's check-outs |
| GET | `/api/reservations/pending-payments` | Get reservations with pending payments |
| GET | `/api/reservations/statistics` | Get reservation statistics |
| POST | `/api/reservations` | Create new reservation |
| PUT | `/api/reservations/{id}` | Update reservation |
| DELETE | `/api/reservations/{id}` | Delete reservation |
| PATCH | `/api/reservations/{id}/confirm` | Confirm reservation |
| PATCH | `/api/reservations/{id}/checkin` | Check in guest |
| PATCH | `/api/reservations/{id}/checkout` | Check out guest |
| PATCH | `/api/reservations/{id}/cancel` | Cancel reservation |
| PATCH | `/api/reservations/{id}/payment` | Update payment status |

## Reservation Status Flow

```
PENDING → CONFIRMED → CHECKED_IN → CHECKED_OUT
    ↓          ↓            ↓
CANCELLED  CANCELLED   NO_SHOW
```

## Payment Status

- PENDING
- PARTIAL_PAID
- PAID
- REFUNDED

## Running the Service

```bash
mvn spring-boot:run
```

## Access Points

- Application: http://localhost:8087
- Swagger UI: http://localhost:8087/swagger-ui.html
- H2 Console: http://localhost:8087/h2-console
  - JDBC URL: jdbc:h2:mem:reservationdb
  - Username: sa
  - Password: password

## Sample Data

The service initializes with 8 sample reservations covering various scenarios:
- Pending reservations
- Confirmed future bookings
- Current guests (checked-in)
- Completed reservations (checked-out)
- Cancelled bookings
- Today's check-ins/check-outs

## Technologies

- Spring Boot 3.2.1
- Java 17
- Spring Data JPA
- H2 Database
- Lombok
- OpenAPI 3.0 (Swagger)
