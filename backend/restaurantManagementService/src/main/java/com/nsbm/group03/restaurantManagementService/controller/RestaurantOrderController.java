package com.nsbm.group03.restaurantManagementService.controller;

import com.nsbm.group03.restaurantManagementService.dto.RestaurantOrderDTO;
import com.nsbm.group03.restaurantManagementService.dto.OrderStatisticsDTO;
import com.nsbm.group03.restaurantManagementService.response.ApiResponse;
import com.nsbm.group03.restaurantManagementService.service.RestaurantOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/orders")
@CrossOrigin(origins = "*")
public class RestaurantOrderController {

    @Autowired
    private RestaurantOrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantOrderDTO>>> getAllOrders() {
        List<RestaurantOrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantOrderDTO>> getOrderById(@PathVariable Long id) {
        RestaurantOrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RestaurantOrderDTO>>> getActiveOrders() {
        List<RestaurantOrderDTO> orders = orderService.getActiveOrders();
        return ResponseEntity.ok(ApiResponse.success("Active orders retrieved successfully", orders));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<OrderStatisticsDTO>> getStatistics() {
        OrderStatisticsDTO stats = orderService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantOrderDTO>> createOrder(@RequestBody RestaurantOrderDTO orderDTO) {
        RestaurantOrderDTO created = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", created));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RestaurantOrderDTO>> updateOrderStatus(
            @PathVariable Long id, @RequestParam String status) {
        RestaurantOrderDTO updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", updated));
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<RestaurantOrderDTO>> updatePaymentStatus(
            @PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String method) {
        RestaurantOrderDTO updated = orderService.updatePaymentStatus(id, status, method);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
    }
}
