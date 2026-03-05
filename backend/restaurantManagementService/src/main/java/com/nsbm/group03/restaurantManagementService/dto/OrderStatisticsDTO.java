package com.nsbm.group03.restaurantManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsDTO {
    private Long totalOrders;
    private Long todaysOrders;
    private Long activeOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private Long pendingOrders;
    private Long roomServiceOrders;
    private Double todaysRevenue;
    private Double totalRevenue;
    private Double averageOrderValue;
}
