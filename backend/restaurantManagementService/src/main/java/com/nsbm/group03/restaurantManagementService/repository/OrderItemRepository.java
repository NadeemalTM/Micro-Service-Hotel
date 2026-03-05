package com.nsbm.group03.restaurantManagementService.repository;

import com.nsbm.group03.restaurantManagementService.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
