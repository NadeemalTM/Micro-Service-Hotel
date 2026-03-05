package com.nsbm.group03.kitchenManagementService.repository;

import com.nsbm.group03.kitchenManagementService.entity.KitchenOrder;
import com.nsbm.group03.kitchenManagementService.entity.KitchenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
    List<KitchenOrder> findByStatus(KitchenStatus status);
    List<KitchenOrder> findByAssignedChef(String chef);
    
    @Query("SELECT k FROM KitchenOrder k WHERE k.status IN ('PENDING', 'IN_QUEUE', 'PREPARING') ORDER BY k.priority ASC, k.receivedAt ASC")
    List<KitchenOrder> findActiveOrders();
    
    @Query("SELECT COUNT(k) FROM KitchenOrder k WHERE k.status = 'PREPARING'")
    Long countOrdersInPreparation();
}
