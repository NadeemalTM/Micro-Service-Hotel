package com.nsbm.group03.roomManagementService.dto;

public class RoomStatisticsDTO {
    private Long totalRooms;
    private Long availableRooms;
    private Long occupiedRooms;
    private Long maintenanceRooms;
    private Long reservedRooms;
    private Double averagePrice;
    private Double totalRevenue;
    private Double occupancyRate;
    
    public RoomStatisticsDTO() {}
    
    public RoomStatisticsDTO(Long totalRooms, Long availableRooms, Long occupiedRooms, 
                              Long maintenanceRooms, Long reservedRooms, Double averagePrice) {
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.occupiedRooms = occupiedRooms;
        this.maintenanceRooms = maintenanceRooms;
        this.reservedRooms = reservedRooms;
        this.averagePrice = averagePrice;
        if (totalRooms > 0) {
            this.occupancyRate = (occupiedRooms.doubleValue() / totalRooms.doubleValue()) * 100;
        }
    }
    
    // Getters and Setters
    public Long getTotalRooms() { return totalRooms; }
    public void setTotalRooms(Long totalRooms) { this.totalRooms = totalRooms; }
    
    public Long getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(Long availableRooms) { this.availableRooms = availableRooms; }
    
    public Long getOccupiedRooms() { return occupiedRooms; }
    public void setOccupiedRooms(Long occupiedRooms) { this.occupiedRooms = occupiedRooms; }
    
    public Long getMaintenanceRooms() { return maintenanceRooms; }
    public void setMaintenanceRooms(Long maintenanceRooms) { this.maintenanceRooms = maintenanceRooms; }
    
    public Long getReservedRooms() { return reservedRooms; }
    public void setReservedRooms(Long reservedRooms) { this.reservedRooms = reservedRooms; }
    
    public Double getAveragePrice() { return averagePrice; }
    public void setAveragePrice(Double averagePrice) { this.averagePrice = averagePrice; }
    
    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public Double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(Double occupancyRate) { this.occupancyRate = occupancyRate; }
}
