# API Quick Reference - Hotel Management Microservices

## Inventory Management Service (Port 8090)

### Base URL
```
http://localhost:8090/api/inventory
```

### Endpoints

#### Get All Items
```http
GET /api/inventory
```
**Response:** Array of inventory items with stock status

#### Get Item by ID
```http
GET /api/inventory/{id}
```
**Example:** `GET /api/inventory/1`

#### Get Low Stock Items
```http
GET /api/inventory/low-stock
```
**Purpose:** Alerts for items below reorder level

#### Create Item
```http
POST /api/inventory
Content-Type: application/json

{
  "name": "Coffee Beans",
  "category": "BEVERAGE",
  "description": "Arabica Coffee Beans",
  "quantity": 20.0,
  "unit": "KG",
  "reorderLevel": 5.0,
  "maxStockLevel": 50.0,
  "unitCost": 12.50,
  "supplier": "Coffee Suppliers Inc",
  "supplierContact": "+1-555-9999",
  "storageLocation": "Dry Storage"
}
```

#### Update Item
```http
PUT /api/inventory/{id}
Content-Type: application/json

{
  "itemCode": "FOOD-001",
  "name": "Basmati Rice Premium",
  "category": "FOOD",
  "quantity": 75.0,
  ...
}
```

#### Adjust Stock (Add/Remove)
```http
PATCH /api/inventory/{id}/adjust-stock?quantity=10&type=ADD
PATCH /api/inventory/{id}/adjust-stock?quantity=5&type=REMOVE
```
**Parameters:**
- `quantity`: Amount to add/remove
- `type`: ADD or REMOVE

**Behavior:**
- ADD: Increases stock, updates lastRestocked timestamp
- REMOVE: Decreases stock (min 0)
- Auto-updates status (IN_STOCK, LOW_STOCK, OUT_OF_STOCK, OVERSTOCKED)

#### Delete Item
```http
DELETE /api/inventory/{id}
```

---

## Event Management Service (Port 8091)

### Base URL
```
http://localhost:8091/api/events
```

### Endpoints

#### Get All Events
```http
GET /api/events
```

#### Get Event by ID
```http
GET /api/events/{id}
```

#### Get Upcoming Events
```http
GET /api/events/upcoming
```
**Filters:** startDate >= today, status != CANCELLED/COMPLETED  
**Sorted by:** startDate ascending

#### Get Events by Type
```http
GET /api/events/type/{type}
```
**Valid Types:**
- WEDDING
- CONFERENCE
- MEETING
- PARTY
- SEMINAR
- WORKSHOP
- CEREMONY
- BANQUET

**Example:** `GET /api/events/type/WEDDING`

#### Get Events by Venue
```http
GET /api/events/venue/{venue}
```
**Valid Venues:**
- GRAND_BALLROOM
- CONFERENCE_ROOM_A
- CONFERENCE_ROOM_B
- GARDEN
- ROOFTOP
- BANQUET_HALL
- MEETING_ROOM

**Example:** `GET /api/events/venue/GRAND_BALLROOM`

#### Create Event
```http
POST /api/events
Content-Type: application/json

{
  "eventName": "Tech Summit 2026",
  "eventType": "CONFERENCE",
  "venue": "CONFERENCE_ROOM_A",
  "startDate": "2026-04-15",
  "endDate": "2026-04-16",
  "startTime": "09:00:00",
  "endTime": "17:00:00",
  "attendeeCount": 100,
  "organizerName": "Tech Events Co",
  "organizerEmail": "events@techco.com",
  "organizerPhone": "+1-555-3000",
  "status": "PLANNED",
  "cateringRequired": true,
  "cateringDetails": "Lunch for 100, vegetarian options",
  "equipmentNeeded": "Projector, microphones, WiFi access",
  "specialRequirements": "Live streaming setup",
  "estimatedCost": 12000.00,
  "depositPaid": 4000.00
}
```

**Auto-Calculated:**
- `balanceDue`: estimatedCost - depositPaid (8000.00)
- `eventCode`: EVT-{timestamp}
- `createdAt`: Current timestamp
- `status`: PLANNED (if not provided)

#### Update Event
```http
PUT /api/events/{id}
Content-Type: application/json
```

#### Update Status
```http
PATCH /api/events/{id}/status?status=CONFIRMED
```
**Valid Statuses:**
- PLANNED
- CONFIRMED
- IN_PROGRESS
- COMPLETED
- CANCELLED

#### Delete Event
```http
DELETE /api/events/{id}
```

---

## Testing with PowerShell

### Inventory Service Tests

```powershell
# Get all inventory items
Invoke-RestMethod http://localhost:8090/api/inventory | ConvertTo-Json -Depth 5

# Get low stock alerts
Invoke-RestMethod http://localhost:8090/api/inventory/low-stock

# Create new item
$item = @{
    name = "Toilet Paper"
    category = "AMENITIES"
    quantity = 200
    unit = "PIECES"
    reorderLevel = 50
    maxStockLevel = 500
    unitCost = 0.50
    supplier = "Supplies Co"
    supplierContact = "+1-555-7777"
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8090/api/inventory -Method POST -Body $item -ContentType "application/json"

# Adjust stock (Add 50 units)
Invoke-RestMethod -Uri "http://localhost:8090/api/inventory/1/adjust-stock?quantity=50&type=ADD" -Method PATCH

# Adjust stock (Remove 10 units)
Invoke-RestMethod -Uri "http://localhost:8090/api/inventory/1/adjust-stock?quantity=10&type=REMOVE" -Method PATCH

# Delete item
Invoke-RestMethod -Uri http://localhost:8090/api/inventory/6 -Method DELETE
```

### Event Service Tests

```powershell
# Get all events
Invoke-RestMethod http://localhost:8091/api/events | ConvertTo-Json -Depth 5

# Get upcoming events only
Invoke-RestMethod http://localhost:8091/api/events/upcoming

# Get weddings
Invoke-RestMethod http://localhost:8091/api/events/type/WEDDING

# Get events at Grand Ballroom
Invoke-RestMethod http://localhost:8091/api/events/venue/GRAND_BALLROOM

# Create new event
$event = @{
    eventName = "Annual Gala"
    eventType = "BANQUET"
    venue = "GRAND_BALLROOM"
    startDate = "2026-06-10"
    endDate = "2026-06-10"
    startTime = "19:00:00"
    endTime = "23:00:00"
    attendeeCount = 200
    organizerName = "Charity Foundation"
    organizerEmail = "info@charity.org"
    organizerPhone = "+1-555-4000"
    status = "PLANNED"
    cateringRequired = $true
    cateringDetails = "Formal dinner, 3 courses"
    estimatedCost = 25000.00
    depositPaid = 10000.00
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8091/api/events -Method POST -Body $event -ContentType "application/json"

# Confirm event
Invoke-RestMethod -Uri "http://localhost:8091/api/events/1/status?status=CONFIRMED" -Method PATCH

# Cancel event
Invoke-RestMethod -Uri "http://localhost:8091/api/events/3/status?status=CANCELLED" -Method PATCH
```

---

## Testing with cURL

### Inventory Service

```bash
# Get all items
curl http://localhost:8090/api/inventory

# Get item by ID
curl http://localhost:8090/api/inventory/1

# Get low stock
curl http://localhost:8090/api/inventory/low-stock

# Create item
curl -X POST http://localhost:8090/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Towels",
    "category": "LINEN",
    "quantity": 100,
    "unit": "PIECES",
    "reorderLevel": 20,
    "maxStockLevel": 200,
    "unitCost": 8.00,
    "supplier": "Textile World"
  }'

# Adjust stock
curl -X PATCH "http://localhost:8090/api/inventory/1/adjust-stock?quantity=25&type=ADD"
```

### Event Service

```bash
# Get all events
curl http://localhost:8091/api/events

# Get upcoming
curl http://localhost:8091/api/events/upcoming

# Create event
curl -X POST http://localhost:8091/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventName": "Workshop",
    "eventType": "WORKSHOP",
    "venue": "MEETING_ROOM",
    "startDate": "2026-05-20",
    "endDate": "2026-05-20",
    "startTime": "10:00:00",
    "endTime": "16:00:00",
    "attendeeCount": 30,
    "organizerName": "Training Dept",
    "organizerEmail": "training@hotel.com",
    "organizerPhone": "+1-555-5000",
    "estimatedCost": 2000.00,
    "depositPaid": 500.00
  }'
```

---

## Expected Responses

### Success Response Format
```json
{
  "success": true,
  "message": "Items retrieved",
  "data": [ /* array of items */ ]
}
```

### Error Response Format
```json
{
  "timestamp": "2026-03-05T15:40:00",
  "status": 404,
  "error": "Not Found",
  "message": "Item not found with id: 999",
  "path": "/api/inventory/999"
}
```

---

## Sample Data IDs

### Inventory Service
- **ID 1:** Rice (FOOD) - 50 KG
- **ID 2:** Orange Juice (BEVERAGE) - 25 L
- **ID 3:** Detergent (CLEANING) - 15 L - LOW STOCK
- **ID 4:** Bed Sheets (LINEN) - 120 PCS
- **ID 5:** Shampoo (AMENITIES) - 8 PCS - LOW STOCK

### Event Service
- **ID 1:** Smith Wedding (WEDDING, Grand Ballroom) - CONFIRMED
- **ID 2:** TechCorp Conference (CONFERENCE, Conference Room A) - CONFIRMED
- **ID 3:** Birthday Party (PARTY, Garden) - PLANNED

---

## Swagger UI Access

**Inventory Service:**  
http://localhost:8090/swagger-ui.html

**Event Service:**  
http://localhost:8091/swagger-ui.html

**Features:**
- Interactive API documentation
- Try-it-out functionality
- Request/response schemas
- HTTP status codes reference

---

## H2 Database Console

**Inventory Service:**  
http://localhost:8090/h2-console
- JDBC URL: `jdbc:h2:mem:inventorydb`
- Username: `sa`
- Password: *(empty)*

**Event Service:**  
http://localhost:8091/h2-console
- JDBC URL: `jdbc:h2:mem:eventdb`
- Username: `sa`
- Password: *(empty)*

**SQL Queries:**
```sql
-- Inventory
SELECT * FROM inventory_items;
SELECT * FROM inventory_items WHERE status = 'LOW_STOCK';
SELECT * FROM inventory_items WHERE quantity <= reorder_level;

-- Events
SELECT * FROM events;
SELECT * FROM events WHERE status = 'CONFIRMED';
SELECT * FROM events WHERE start_date >= CURRENT_DATE;
```

---

## Common HTTP Status Codes

- **200 OK:** Successful GET, PUT, PATCH
- **201 CREATED:** Successful POST
- **204 NO CONTENT:** Successful DELETE
- **400 BAD REQUEST:** Invalid input data
- **404 NOT FOUND:** Resource doesn't exist
- **500 INTERNAL SERVER ERROR:** Server-side error

---

## Tips for Testing

1. **Start services in order:** Employee (8085) first for auth, then others
2. **Wait for startup:** Allow 20-30 seconds for Spring Boot initialization
3. **Check logs:** Watch console for "Started {Service}Application" message
4. **Verify sample data:** Check counts match expected (5 inventory, 3 events)
5. **Test workflows:** Create → Read → Update → Delete
6. **Test validations:** Try invalid status codes, negative quantities
7. **Test relationships:** Adjust stock and verify status auto-update
8. **Check calculations:** Verify balanceDue = estimatedCost - depositPaid

---

**Quick Health Check Script:**
```powershell
$services = @(8085, 8086, 8087, 8088, 8089, 8090, 8091)
foreach ($port in $services) {
    try {
        $response = Invoke-WebRequest "http://localhost:$port/actuator/health" -UseBasicParsing -TimeoutSec 2
        Write-Host "✅ Port $port - UP" -ForegroundColor Green
    } catch {
        Write-Host "❌ Port $port - DOWN" -ForegroundColor Red
    }
}
```

---

**Last Updated:** March 5, 2026  
**Coverage:** Inventory & Event Services  
**Format:** REST API with JSON  
**Authentication:** Currently open (add JWT for production)
