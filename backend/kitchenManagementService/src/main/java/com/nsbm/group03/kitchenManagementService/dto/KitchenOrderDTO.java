package com.nsbm.group03.kitchenManagementService.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderDTO {
    private Long id;
    private String orderNumber;
    private Long restaurantOrderId;
    private Integer tableNumber;
    private Long roomNumber;
    private String orderType;
    private String items;
    private String status;
    private String assignedChef;
    private Integer priority;
    private Integer estimatedTime;
    private String specialInstructions;
    private LocalDateTime receivedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer actualPrepTime;
}
