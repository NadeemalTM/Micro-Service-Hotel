package com.nsbm.group03.inventoryManagementService.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String itemCode;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String category; // FOOD, BEVERAGE, CLEANING, LINEN, AMENITIES, EQUIPMENT
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Double quantity;
    
    @Column(nullable = false)
    private String unit; // KG, LITERS, PIECES, BOXES, etc.
    
    @Column(nullable = false)
    private Double reorderLevel;
    
    @Column(nullable = false)
    private Double maxStockLevel;
    
    @Column(nullable = false)
    private Double unitCost;
    
    @Column
    private String supplier;
    
    @Column
    private String supplierContact;
    
    @Column
    @Enumerated(EnumType.STRING)
    private StockStatus status;
    
    @Column
    private String storageLocation;
    
    @Column
    private LocalDateTime lastRestocked;
    
    @Column
    private LocalDateTime expiryDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            updateStatus();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStatus();
    }
    
    private void updateStatus() {
        if (quantity <= 0) {
            status = StockStatus.OUT_OF_STOCK;
        } else if (quantity <= reorderLevel) {
            status = StockStatus.LOW_STOCK;
        } else if (quantity >= maxStockLevel) {
            status = StockStatus.OVERSTOCKED;
        } else {
            status = StockStatus.IN_STOCK;
        }
    }
}
