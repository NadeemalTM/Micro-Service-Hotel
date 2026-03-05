package com.nsbm.group03.restaurantManagementService.repository;

import com.nsbm.group03.restaurantManagementService.entity.RestaurantOrder;
import com.nsbm.group03.restaurantManagementService.entity.OrderStatus;
import com.nsbm.group03.restaurantManagementService.entity.OrderType;
import com.nsbm.group03.restaurantManagementService.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {
    
    Optional<RestaurantOrder> findByOrderNumber(String orderNumber);
    
    List<RestaurantOrder> findByTableNumber(Integer tableNumber);
    
    List<RestaurantOrder> findByStatus(OrderStatus status);
    
    List<RestaurantOrder> findByOrderType(OrderType orderType);
    
    List<RestaurantOrder> findByPaymentStatus(PaymentStatus paymentStatus);
    
    List<RestaurantOrder> findByRoomNumber(Long roomNumber);
    
    List<RestaurantOrder> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT o FROM RestaurantOrder o WHERE o.status IN ('PENDING', 'CONFIRMED', 'PREPARING')")
    List<RestaurantOrder> findActiveOrders();
    
    @Query("SELECT o FROM RestaurantOrder o WHERE o.orderType = 'ROOM_SERVICE' AND o.status IN ('PENDING', 'PREPARING', 'READY')")
    List<RestaurantOrder> findPendingRoomServiceOrders();
    
    @Query("SELECT o FROM RestaurantOrder o WHERE o.status = 'READY'")
    List<RestaurantOrder> findReadyOrders();
    
    @Query("SELECT COUNT(o) FROM RestaurantOrder o WHERE FUNCTION('DATE', o.orderTime) = CURRENT_DATE")
    Long countTodaysOrders();
    
    @Query("SELECT SUM(o.finalAmount) FROM RestaurantOrder o WHERE FUNCTION('DATE', o.orderTime) = CURRENT_DATE AND o.status != 'CANCELLED'")
    Double todaysRevenue();
    
    @Query("SELECT SUM(o.finalAmount) FROM RestaurantOrder o WHERE o.orderTime >= :startDate AND o.status != 'CANCELLED'")
    Double revenueFrom(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT AVG(o.finalAmount) FROM RestaurantOrder o WHERE o.status != 'CANCELLED'")
    Double averageOrderValue();
}
