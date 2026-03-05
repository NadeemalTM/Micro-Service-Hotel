package com.nsbm.group03.inventoryManagementService.service;

import com.nsbm.group03.inventoryManagementService.dto.InventoryItemDTO;
import com.nsbm.group03.inventoryManagementService.entity.InventoryItem;
import com.nsbm.group03.inventoryManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.inventoryManagementService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    public List<InventoryItemDTO> getAllItems() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public InventoryItemDTO getItemById(Long id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id)));
    }

    public List<InventoryItemDTO> getLowStockItems() {
        return repository.findLowStockItems().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public InventoryItemDTO createItem(InventoryItemDTO dto) {
        InventoryItem item = new InventoryItem();
        updateEntity(item, dto);
        if (dto.getItemCode() == null) {
            item.setItemCode("ITEM-" + System.currentTimeMillis());
        }
        return toDTO(repository.save(item));
    }

    public InventoryItemDTO updateItem(Long id, InventoryItemDTO dto) {
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        updateEntity(item, dto);
        return toDTO(repository.save(item));
    }

    public InventoryItemDTO adjustStock(Long id, Double quantityChange, String type) {
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        
        if ("ADD".equals(type)) {
            item.setQuantity(item.getQuantity() + quantityChange);
            item.setLastRestocked(LocalDateTime.now());
        } else if ("REMOVE".equals(type)) {
            item.setQuantity(Math.max(0, item.getQuantity() - quantityChange));
        }
        
        return toDTO(repository.save(item));
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    private void updateEntity(InventoryItem item, InventoryItemDTO dto) {
        item.setItemCode(dto.getItemCode());
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setDescription(dto.getDescription());
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setReorderLevel(dto.getReorderLevel());
        item.setMaxStockLevel(dto.getMaxStockLevel());
        item.setUnitCost(dto.getUnitCost());
        item.setSupplier(dto.getSupplier());
        item.setSupplierContact(dto.getSupplierContact());
        item.setStorageLocation(dto.getStorageLocation());
        item.setExpiryDate(dto.getExpiryDate());
    }

    private InventoryItemDTO toDTO(InventoryItem item) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(item.getId());
        dto.setItemCode(item.getItemCode());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setDescription(item.getDescription());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setReorderLevel(item.getReorderLevel());
        dto.setMaxStockLevel(item.getMaxStockLevel());
        dto.setUnitCost(item.getUnitCost());
        dto.setSupplier(item.getSupplier());
        dto.setSupplierContact(item.getSupplierContact());
        dto.setStatus(item.getStatus() != null ? item.getStatus().name() : null);
        dto.setStorageLocation(item.getStorageLocation());
        dto.setLastRestocked(item.getLastRestocked());
        dto.setExpiryDate(item.getExpiryDate());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}
