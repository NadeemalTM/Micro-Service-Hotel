package com.nsbm.group03.restaurantManagementService.service;

import com.nsbm.group03.restaurantManagementService.dto.*;
import com.nsbm.group03.restaurantManagementService.entity.*;
import com.nsbm.group03.restaurantManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.restaurantManagementService.repository.RestaurantOrderRepository;
import com.nsbm.group03.restaurantManagementService.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantOrderService {

    @Autowired
    private RestaurantOrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<RestaurantOrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RestaurantOrderDTO getOrderById(Long id) {
        RestaurantOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    public List<RestaurantOrderDTO> getActiveOrders() {
        return orderRepository.findActiveOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RestaurantOrderDTO createOrder(RestaurantOrderDTO dto) {
        RestaurantOrder order = new RestaurantOrder();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setTableNumber(dto.getTableNumber());
        order.setGuestName(dto.getGuestName());
        order.setRoomNumber(dto.getRoomNumber());
        order.setOrderType(dto.getOrderType());
        order.setSpecialInstructions(dto.getSpecialInstructions());
        order.setServerName(dto.getServerName());
        order.setGuestCount(dto.getGuestCount());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);

        // Add items
        double subtotal = 0;
        for (OrderItemDTO itemDTO : dto.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
            
            OrderItem orderItem = new OrderItem(
                    order,
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getCategory(),
                    itemDTO.getQuantity(),
                    menuItem.getPrice(),
                    itemDTO.getSpecialInstructions()
            );
            order.getItems().add(orderItem);
            subtotal += orderItem.getTotalPrice();
        }

        order.setSubtotal(subtotal);
        order.setTaxAmount(subtotal * 0.1); // 10% tax
        order.setServiceCharge(subtotal * 0.05); // 5% service charge
        order.setTotalAmount(subtotal + order.getTaxAmount() + order.getServiceCharge());
        order.setDiscountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0.0);
        order.setFinalAmount(order.getTotalAmount() - order.getDiscountAmount());

        RestaurantOrder saved = orderRepository.save(order);
        return convertToDTO(saved);
    }

    public RestaurantOrderDTO updateOrderStatus(Long id, String status) {
        RestaurantOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(orderStatus);
        
        if (orderStatus == OrderStatus.READY) {
            order.setPreparedTime(LocalDateTime.now());
        } else if (orderStatus == OrderStatus.SERVED) {
            order.setServedTime(LocalDateTime.now());
        } else if (orderStatus == OrderStatus.COMPLETED) {
            order.setCompletedTime(LocalDateTime.now());
        }
        
        RestaurantOrder updated = orderRepository.save(order);
        return convertToDTO(updated);
    }

    public RestaurantOrderDTO updatePaymentStatus(Long id, String paymentStatus, String method) {
        RestaurantOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        order.setPaymentStatus(PaymentStatus.valueOf(paymentStatus.toUpperCase()));
        order.setPaymentMethod(method);
        
        RestaurantOrder updated = orderRepository.save(order);
        return convertToDTO(updated);
    }

    public OrderStatisticsDTO getStatistics() {
        List<RestaurantOrder> all = orderRepository.findAll();
        
        OrderStatisticsDTO stats = new OrderStatisticsDTO();
        stats.setTotalOrders((long) all.size());
        stats.setTodaysOrders(orderRepository.countTodaysOrders());
        stats.setActiveOrders((long) orderRepository.findActiveOrders().size());
        stats.setCompletedOrders(all.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).count());
        stats.setCancelledOrders(all.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count());
        stats.setPendingOrders(all.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count());
        stats.setRoomServiceOrders(all.stream().filter(o -> o.getOrderType() == OrderType.ROOM_SERVICE).count());
        stats.setTodaysRevenue(orderRepository.todaysRevenue() != null ? orderRepository.todaysRevenue() : 0.0);
        stats.setTotalRevenue(all.stream().filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(RestaurantOrder::getFinalAmount).sum());
        stats.setAverageOrderValue(orderRepository.averageOrderValue() != null ? orderRepository.averageOrderValue() : 0.0);
        
        return stats;
    }

    public void deleteOrder(Long id) {
        RestaurantOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private RestaurantOrderDTO convertToDTO(RestaurantOrder order) {
        RestaurantOrderDTO dto = new RestaurantOrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setTableNumber(order.getTableNumber());
        dto.setGuestName(order.getGuestName());
        dto.setRoomNumber(order.getRoomNumber());
        dto.setOrderType(order.getOrderType());
        dto.setStatus(order.getStatus());
        dto.setSubtotal(order.getSubtotal());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setServiceCharge(order.getServiceCharge());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setFinalAmount(order.getFinalAmount());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTransactionId(order.getTransactionId());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setServerName(order.getServerName());
        dto.setPreparedBy(order.getPreparedBy());
        dto.setOrderTime(order.getOrderTime());
        dto.setPreparedTime(order.getPreparedTime());
        dto.setServedTime(order.getServedTime());
        dto.setCompletedTime(order.getCompletedTime());
        dto.setEstimatedPrepTime(order.getEstimatedPrepTime());
        dto.setGuestCount(order.getGuestCount());
        dto.setNotes(order.getNotes());
        
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setMenuItemId(item.getMenuItemId());
                    itemDTO.setItemName(item.getItemName());
                    itemDTO.setCategory(item.getCategory());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setUnitPrice(item.getUnitPrice());
                    itemDTO.setTotalPrice(item.getTotalPrice());
                    itemDTO.setSpecialInstructions(item.getSpecialInstructions());
                    itemDTO.setItemStatus(item.getItemStatus() != null ? item.getItemStatus().name() : null);
                    return itemDTO;
                })
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);
        
        return dto;
    }
}
