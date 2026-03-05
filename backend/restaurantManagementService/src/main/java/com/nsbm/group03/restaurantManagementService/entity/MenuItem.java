package com.nsbm.group03.restaurantManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String category; // APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, BREAKFAST, LUNCH, DINNER
    
    @Column(nullable = false)
    private Double price;
    
    @Column
    private String cuisine; // ITALIAN, CHINESE, INDIAN, AMERICAN, etc.
    
    @Column(nullable = false)
    private Boolean available;
    
    @Column(nullable = false)
    private Boolean vegetarian;
    
    @Column(nullable = false)
    private Boolean vegan;
    
    @Column
    private Boolean glutenFree;
    
    @Column
    private Boolean spicy;
    
    @Column
    private Integer preparationTime; // in minutes
    
    @Column
    private Integer calories;
    
    @Column
    private String imageUrl;
    
    @Column
    private String ingredients;
    
    @Column
    private Integer popularityScore;
}
