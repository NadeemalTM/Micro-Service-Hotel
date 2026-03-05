package com.nsbm.group03.restaurantManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long menuItemId;
    private String itemName;
    private String category;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String specialInstructions;
    private String itemStatus;
}
