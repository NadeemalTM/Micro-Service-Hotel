package com.nsbm.group03.kitchenManagementService.controller;

import com.nsbm.group03.kitchenManagementService.dto.KitchenOrderDTO;
import com.nsbm.group03.kitchenManagementService.response.ApiResponse;
import com.nsbm.group03.kitchenManagementService.service.KitchenOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kitchen/orders")
@CrossOrigin(origins = "*")
public class KitchenOrderController {

    @Autowired
    private KitchenOrderService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<KitchenOrderDTO>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved", service.getAllOrders()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<KitchenOrderDTO>>> getActiveOrders() {
        return ResponseEntity.ok(ApiResponse.success("Active orders retrieved", service.getActiveOrders()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KitchenOrderDTO>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order retrieved", service.getOrderById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KitchenOrderDTO>> createOrder(@RequestBody KitchenOrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created", service.createOrder(dto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<KitchenOrderDTO>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", service.updateStatus(id, status)));
    }

    @PatchMapping("/{id}/assign-chef")
    public ResponseEntity<ApiResponse<KitchenOrderDTO>> assignChef(@PathVariable Long id, @RequestParam String chef) {
        return ResponseEntity.ok(ApiResponse.success("Chef assigned", service.assignChef(id, chef)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted", null));
    }
}
