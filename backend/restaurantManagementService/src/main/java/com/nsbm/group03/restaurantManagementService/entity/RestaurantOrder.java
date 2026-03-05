package com.nsbm.group03.restaurantManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @Column
    private Integer tableNumber;
    
    @Column
    private String guestName;
    
    @Column
    private Long roomNumber;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType; // DINE_IN, ROOM_SERVICE, TAKEAWAY
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(nullable = false)
    private Double subtotal;
    
    @Column
    private Double taxAmount;
    
    @Column
    private Double serviceCharge;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column
    private Double discountAmount;
    
    @Column(nullable = false)
    private Double finalAmount;
    
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    @Column
    private String paymentMethod;
    
    @Column
    private String transactionId;
    
    @Column(length = 1000)
    private String specialInstructions;
    
    @Column
    private String serverName;
    
    @Column
    private String preparedBy;
    
    @Column(nullable = false)
    private LocalDateTime orderTime;
    
    @Column
    private LocalDateTime preparedTime;
    
    @Column
    private LocalDateTime servedTime;
    
    @Column
    private LocalDateTime completedTime;
    
    @Column
    private Integer estimatedPrepTime; // in minutes
    
    @Column
    private Integer guestCount;
    
    @Column
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.UNPAID;
        }
        if (orderNumber == null) {
            orderNumber = "ORD-" + System.currentTimeMillis();
        }
    }
}
