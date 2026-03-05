package com.nsbm.group03.roomManagementService.dto;

public class RoomDTO {
    private Long id;
    private String roomNumber;
    private Integer floor;
    private String roomType;
    private String status;
    private Double pricePerNight;
    private Integer capacity;
    private Integer bedCount;
    private String bedType;
    private Boolean hasBalcony;
    private Boolean hasSeaView;
    private Double roomSize;
    private String amenities;
    private String description;
    
    // Constructors
    public RoomDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public Integer getBedCount() { return bedCount; }
    public void setBedCount(Integer bedCount) { this.bedCount = bedCount; }
    
    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }
    
    public Boolean getHasBalcony() { return hasBalcony; }
    public void setHasBalcony(Boolean hasBalcony) { this.hasBalcony = hasBalcony; }
    
    public Boolean getHasSeaView() { return hasSeaView; }
    public void setHasSeaView(Boolean hasSeaView) { this.hasSeaView = hasSeaView; }
    
    public Double getRoomSize() { return roomSize; }
    public void setRoomSize(Double roomSize) { this.roomSize = roomSize; }
    
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
