package com.nsbm.group03.roomManagementService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String roomNumber;
    
    @Column(nullable = false)
    private Integer floor;
    
    @Column(nullable = false)
    private String roomType; // STANDARD, DELUXE, SUITE, EXECUTIVE, PRESIDENTIAL
    
    @Column(nullable = false)
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED, OUT_OF_SERVICE
    
    @Column(nullable = false)
    private Double pricePerNight;
    
    @Column(nullable = false)
    private Integer capacity; // Number of guests
    
    @Column(nullable = false)
    private Integer bedCount;
    
    @Column(nullable = false)
    private String bedType; // SINGLE, DOUBLE, QUEEN, KING
    
    @Column(nullable = false)
    private Boolean hasBalcony = false;
    
    @Column(nullable = false)
    private Boolean hasSeaView = false;
    
    @Column(nullable = false)
    private Double roomSize; // In square meters
    
    @Column(length = 1000)
    private String amenities; // Comma-separated list
    
    @Column(length = 500)
    private String description;
    
    @Column
    private LocalDateTime lastCleaned;
    
    @Column
    private LocalDateTime lastMaintenance;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    // Constructors
    public Room() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Room(String roomNumber, Integer floor, String roomType, String status, 
                Double pricePerNight, Integer capacity, Integer bedCount, String bedType) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.roomType = roomType;
        this.status = status;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.bedCount = bedCount;
        this.bedType = bedType;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public Integer getFloor() {
        return floor;
    }
    
    public void setFloor(Integer floor) {
        this.floor = floor;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Integer getBedCount() {
        return bedCount;
    }
    
    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }
    
    public String getBedType() {
        return bedType;
    }
    
    public void setBedType(String bedType) {
        this.bedType = bedType;
    }
    
    public Boolean getHasBalcony() {
        return hasBalcony;
    }
    
    public void setHasBalcony(Boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }
    
    public Boolean getHasSeaView() {
        return hasSeaView;
    }
    
    public void setHasSeaView(Boolean hasSeaView) {
        this.hasSeaView = hasSeaView;
    }
    
    public Double getRoomSize() {
        return roomSize;
    }
    
    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getLastCleaned() {
        return lastCleaned;
    }
    
    public void setLastCleaned(LocalDateTime lastCleaned) {
        this.lastCleaned = lastCleaned;
    }
    
    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }
    
    public void setLastMaintenance(LocalDateTime lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
