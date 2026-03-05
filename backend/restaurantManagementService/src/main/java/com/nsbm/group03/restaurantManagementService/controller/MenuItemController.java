package com.nsbm.group03.restaurantManagementService.controller;

import com.nsbm.group03.restaurantManagementService.dto.MenuItemDTO;
import com.nsbm.group03.restaurantManagementService.response.ApiResponse;
import com.nsbm.group03.restaurantManagementService.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/menu")
@CrossOrigin(origins = "*")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemDTO>>> getAllMenuItems() {
        List<MenuItemDTO> items = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved successfully", items));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<MenuItemDTO>>> getAvailableMenuItems() {
        List<MenuItemDTO> items = menuItemService.getAvailableMenuItems();
        return ResponseEntity.ok(ApiResponse.success("Available menu items retrieved successfully", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDTO>> getMenuItemById(@PathVariable Long id) {
        MenuItemDTO item = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Menu item retrieved successfully", item));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<MenuItemDTO>>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> items = menuItemService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved successfully", items));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemDTO>> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO created = menuItemService.createMenuItem(menuItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu item created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDTO>> updateMenuItem(
            @PathVariable Long id, @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO updated = menuItemService.updateMenuItem(id, menuItemDTO);
        return ResponseEntity.ok(ApiResponse.success("Menu item updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success("Menu item deleted successfully", null));
    }
}
