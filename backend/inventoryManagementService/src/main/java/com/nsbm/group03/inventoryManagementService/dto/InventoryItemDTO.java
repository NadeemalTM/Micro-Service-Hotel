package com.nsbm.group03.inventoryManagementService.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private Long id;
    private String itemCode;
    private String name;
    private String category;
    private String description;
    private Double quantity;
    private String unit;
    private Double reorderLevel;
    private Double maxStockLevel;
    private Double unitCost;
    private String supplier;
    private String supplierContact;
    private String status;
    private String storageLocation;
    private LocalDateTime lastRestocked;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
