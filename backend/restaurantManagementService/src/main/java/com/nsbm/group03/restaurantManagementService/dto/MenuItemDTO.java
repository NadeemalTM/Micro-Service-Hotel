package com.nsbm.group03.restaurantManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String cuisine;
    private Boolean available;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean spicy;
    private Integer preparationTime;
    private Integer calories;
    private String imageUrl;
    private String ingredients;
    private Integer popularityScore;
}
