package com.nsbm.group03.kitchenManagementService.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kitchen_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @Column(nullable = false)
    private Long restaurantOrderId;
    
    @Column
    private Integer tableNumber;
    
    @Column
    private Long roomNumber;
    
    @Column(nullable = false)
    private String orderType; // DINE_IN, ROOM_SERVICE, TAKEAWAY
    
    @Column(length = 2000)
    private String items;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KitchenStatus status;
    
    @Column
    private String assignedChef;
    
    @Column(nullable = false)
    private Integer priority; // 1=High, 2=Medium, 3=Low
    
    @Column
    private Integer estimatedTime; // minutes
    
    @Column(length = 1000)
    private String specialInstructions;
    
    @Column(nullable = false)
    private LocalDateTime receivedAt;
    
    @Column
    private LocalDateTime startedAt;
    
    @Column
    private LocalDateTime completedAt;
    
    @Column
    private Integer actualPrepTime;
    
    @PrePersist
    protected void onCreate() {
        receivedAt = LocalDateTime.now();
        if (status == null) {
            status = KitchenStatus.PENDING;
        }
        if (priority == null) {
            priority = 2;
        }
    }
}
