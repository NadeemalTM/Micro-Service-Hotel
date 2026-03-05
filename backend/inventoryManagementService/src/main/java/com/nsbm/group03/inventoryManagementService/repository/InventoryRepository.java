package com.nsbm.group03.inventoryManagementService.repository;

import com.nsbm.group03.inventoryManagementService.entity.InventoryItem;
import com.nsbm.group03.inventoryManagementService.entity.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByItemCode(String itemCode);
    List<InventoryItem> findByCategory(String category);
    List<InventoryItem> findByStatus(StockStatus status);
    List<InventoryItem> findBySupplier(String supplier);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderLevel")
    List<InventoryItem> findLowStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE i.status = 'OUT_OF_STOCK'")
    List<InventoryItem> findOutOfStockItems();
    
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity <= i.reorderLevel")
    Long countLowStockItems();
    
    @Query("SELECT SUM(i.quantity * i.unitCost) FROM InventoryItem i")
    Double totalInventoryValue();
}
