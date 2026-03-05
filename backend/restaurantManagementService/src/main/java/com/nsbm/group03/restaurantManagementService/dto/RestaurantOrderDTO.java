package com.nsbm.group03.restaurantManagementService.dto;

import com.nsbm.group03.restaurantManagementService.entity.OrderStatus;
import com.nsbm.group03.restaurantManagementService.entity.OrderType;
import com.nsbm.group03.restaurantManagementService.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOrderDTO {
    private Long id;
    private String orderNumber;
    private Integer tableNumber;
    private String guestName;
    private Long roomNumber;
    private OrderType orderType;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private Double subtotal;
    private Double taxAmount;
    private Double serviceCharge;
    private Double totalAmount;
    private Double discountAmount;
    private Double finalAmount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private String specialInstructions;
    private String serverName;
    private String preparedBy;
    private LocalDateTime orderTime;
    private LocalDateTime preparedTime;
    private LocalDateTime servedTime;
    private LocalDateTime completedTime;
    private Integer estimatedPrepTime;
    private Integer guestCount;
    private String notes;
}
