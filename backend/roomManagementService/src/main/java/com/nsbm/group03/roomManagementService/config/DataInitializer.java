package com.nsbm.group03.roomManagementService.config;

import com.nsbm.group03.roomManagementService.entity.Room;
import com.nsbm.group03.roomManagementService.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            initializeRooms();
        }
    }
    
    private void initializeRooms() {
        // Floor 1 - Standard Rooms (Sigiriya Theme)
        createRoom("101", 1, "STANDARD", "AVAILABLE", 12500.00, 2, 1, "QUEEN", false, false, 25.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Tea/Coffee Maker", "Comfortable room with Sigiriya rock artwork");
        createRoom("102", 1, "STANDARD", "AVAILABLE", 12500.00, 2, 1, "QUEEN", false, false, 25.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Tea/Coffee Maker", "Traditional Sri Lankan decor");
        createRoom("103", 1, "STANDARD", "OCCUPIED", 11000.00, 2, 2, "SINGLE", false, false, 28.0, 
                   "Free WiFi, Smart TV, AC, Mini Fridge", "Twin bed room with Ceylon tea collection");
        createRoom("104", 1, "DELUXE", "AVAILABLE", 18500.00, 2, 1, "KING", true, false, 35.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Balcony, Coffee Machine, Safe", "Deluxe room with private balcony overlooking garden");
        
        // Floor 2 - Deluxe Rooms (Galle Fort Theme)
        createRoom("201", 2, "DELUXE", "AVAILABLE", 22000.00, 2, 1, "KING", true, true, 35.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Balcony, Coffee Machine, Indian Ocean View", "Stunning ocean view room with colonial architecture");
        createRoom("202", 2, "DELUXE", "MAINTENANCE", 22000.00, 2, 1, "KING", true, true, 35.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Balcony, Coffee Machine, Ocean View", "Galle Fort inspired design with ocean vista");
        createRoom("203", 2, "DELUXE", "AVAILABLE", 20000.00, 3, 1, "KING", true, false, 38.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Balcony, Sofa Bed, Kitchen", "Family deluxe with traditional Kandyan art");
        createRoom("204", 2, "SUITE", "AVAILABLE", 35000.00, 4, 2, "KING", true, true, 60.0, 
                   "Free WiFi, Smart TV, AC, Mini Bar, Balcony, Living Room, Kitchenette, Ocean View", "Junior suite with separate living area and sea view");
        
        // Floor 3 - Executive Suites (Temple of Tooth Theme)
        createRoom("301", 3, "SUITE", "RESERVED", 38000.00, 4, 2, "KING", true, true, 60.0, 
                   "Free WiFi, Smart TV, AC, Premium Mini Bar, Balcony, Living Room, Kitchenette, Ocean View", "Elegant suite with Kandyan cultural elements");
        createRoom("302", 3, "EXECUTIVE", "AVAILABLE", 55000.00, 4, 2, "KING", true, true, 75.0, 
                   "Free WiFi, Smart TV, AC, Premium Mini Bar, Large Balcony, Living Room, Full Kitchen, Dining Area, Ocean View, Jacuzzi", "Executive suite with traditional woodwork and modern amenities");
        createRoom("303", 3, "EXECUTIVE", "AVAILABLE", 55000.00, 4, 2, "KING", true, true, 75.0, 
                   "Free WiFi, Smart TV, AC, Premium Mini Bar, Large Balcony, Living Room, Full Kitchen, Dining Area, Ocean View, Jacuzzi", "Luxurious suite featuring Sri Lankan batik art");
        
        // Floor 4 - Presidential Suite (Adam's Peak Theme)
        createRoom("401", 4, "PRESIDENTIAL", "AVAILABLE", 125000.00, 6, 3, "KING", true, true, 150.0, 
                   "Free WiFi, Multiple Smart TVs, Full AC, Premium Bar, Multiple Balconies, Grand Living Room, Full Kitchen, Formal Dining Room, Private Office, Master Suite, 2 Guest Bedrooms, Panoramic Ocean View, Private Jacuzzi, Butler Service, Private Terrace", "Prestigious presidential suite with panoramic Indian Ocean views and traditional Sri Lankan luxury");
        
        System.out.println("✅ Initialized 12 rooms with Sri Lankan themes successfully!");
    }
    
    private void createRoom(String roomNumber, Integer floor, String roomType, String status, 
                            Double pricePerNight, Integer capacity, Integer bedCount, String bedType,
                            Boolean hasBalcony, Boolean hasSeaView, Double roomSize, 
                            String amenities, String description) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setFloor(floor);
        room.setRoomType(roomType);
        room.setStatus(status);
        room.setPricePerNight(pricePerNight);
        room.setCapacity(capacity);
        room.setBedCount(bedCount);
        room.setBedType(bedType);
        room.setHasBalcony(hasBalcony);
        room.setHasSeaView(hasSeaView);
        room.setRoomSize(roomSize);
        room.setAmenities(amenities);
        room.setDescription(description);
        room.setCreatedAt(LocalDateTime.now());
        room.setLastCleaned(LocalDateTime.now());
        roomRepository.save(room);
    }
}
