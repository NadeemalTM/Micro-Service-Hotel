package com.nsbm.group03.restaurantManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private RestaurantOrder order;
    
    @Column(nullable = false)
    private Long menuItemId;
    
    @Column(nullable = false)
    private String itemName;
    
    @Column
    private String category;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double unitPrice;
    
    @Column(nullable = false)
    private Double totalPrice;
    
    @Column(length = 500)
    private String specialInstructions;
    
    @Column
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus; // ORDERED, PREPARING, READY, SERVED
    
    public OrderItem(RestaurantOrder order, Long menuItemId, String itemName, String category, 
                     Integer quantity, Double unitPrice, String specialInstructions) {
        this.order = order;
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
        this.specialInstructions = specialInstructions;
        this.itemStatus = ItemStatus.ORDERED;
    }
}
