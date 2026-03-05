package com.nsbm.group03.inventoryManagementService.controller;

import com.nsbm.group03.inventoryManagementService.dto.InventoryItemDTO;
import com.nsbm.group03.inventoryManagementService.response.ApiResponse;
import com.nsbm.group03.inventoryManagementService.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryItemDTO>>> getAllItems() {
        return ResponseEntity.ok(ApiResponse.success("Items retrieved", service.getAllItems()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Item retrieved", service.getItemById(id)));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDTO>>> getLowStockItems() {
        return ResponseEntity.ok(ApiResponse.success("Low stock items retrieved", service.getLowStockItems()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemDTO>> createItem(@RequestBody InventoryItemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item created", service.createItem(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> updateItem(@PathVariable Long id, @RequestBody InventoryItemDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Item updated", service.updateItem(id, dto)));
    }

    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> adjustStock(
            @PathVariable Long id, @RequestParam Double quantity, @RequestParam String type) {
        return ResponseEntity.ok(ApiResponse.success("Stock adjusted", service.adjustStock(id, quantity, type)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted", null));
    }
}
