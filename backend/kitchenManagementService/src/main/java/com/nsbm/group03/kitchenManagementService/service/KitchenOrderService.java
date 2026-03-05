package com.nsbm.group03.kitchenManagementService.service;

import com.nsbm.group03.kitchenManagementService.dto.KitchenOrderDTO;
import com.nsbm.group03.kitchenManagementService.entity.*;
import com.nsbm.group03.kitchenManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.kitchenManagementService.repository.KitchenOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KitchenOrderService {

    @Autowired
    private KitchenOrderRepository repository;

    public List<KitchenOrderDTO> getAllOrders() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<KitchenOrderDTO> getActiveOrders() {
        return repository.findActiveOrders().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public KitchenOrderDTO getOrderById(Long id) {
        KitchenOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen order not found with id: " + id));
        return convertToDTO(order);
    }

    public KitchenOrderDTO createOrder(KitchenOrderDTO dto) {
        KitchenOrder order = new KitchenOrder();
        order.setOrderNumber(dto.getOrderNumber() != null ? dto.getOrderNumber() : "KO-" + System.currentTimeMillis());
        order.setRestaurantOrderId(dto.getRestaurantOrderId());
        order.setTableNumber(dto.getTableNumber());
        order.setRoomNumber(dto.getRoomNumber());
        order.setOrderType(dto.getOrderType());
        order.setItems(dto.getItems());
        order.setStatus(KitchenStatus.PENDING);
        order.setPriority(dto.getPriority() != null ? dto.getPriority() : 2);
        order.setEstimatedTime(dto.getEstimatedTime());
        order.setSpecialInstructions(dto.getSpecialInstructions());
        
        KitchenOrder saved = repository.save(order);
        return convertToDTO(saved);
    }

    public KitchenOrderDTO updateStatus(Long id, String status) {
        KitchenOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen order not found"));
        
        KitchenStatus newStatus = KitchenStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);
        
        if (newStatus == KitchenStatus.PREPARING && order.getStartedAt() == null) {
            order.setStartedAt(LocalDateTime.now());
        } else if (newStatus == KitchenStatus.READY && order.getCompletedAt() == null) {
            order.setCompletedAt(LocalDateTime.now());
            if (order.getStartedAt() != null) {
                order.setActualPrepTime((int) ChronoUnit.MINUTES.between(order.getStartedAt(), order.getCompletedAt()));
            }
        }
        
        KitchenOrder updated = repository.save(order);
        return convertToDTO(updated);
    }

    public KitchenOrderDTO assignChef(Long id, String chefName) {
        KitchenOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen order not found"));
        order.setAssignedChef(chefName);
        KitchenOrder updated = repository.save(order);
        return convertToDTO(updated);
    }

    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }

    private KitchenOrderDTO convertToDTO(KitchenOrder order) {
        KitchenOrderDTO dto = new KitchenOrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setRestaurantOrderId(order.getRestaurantOrderId());
        dto.setTableNumber(order.getTableNumber());
        dto.setRoomNumber(order.getRoomNumber());
        dto.setOrderType(order.getOrderType());
        dto.setItems(order.getItems());
        dto.setStatus(order.getStatus().name());
        dto.setAssignedChef(order.getAssignedChef());
        dto.setPriority(order.getPriority());
        dto.setEstimatedTime(order.getEstimatedTime());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setReceivedAt(order.getReceivedAt());
        dto.setStartedAt(order.getStartedAt());
        dto.setCompletedAt(order.getCompletedAt());
        dto.setActualPrepTime(order.getActualPrepTime());
        return dto;
    }
}
