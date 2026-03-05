package com.nsbm.group03.inventoryManagementService.config;

import com.nsbm.group03.inventoryManagementService.entity.InventoryItem;
import com.nsbm.group03.inventoryManagementService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InventoryRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            // Food Items
            repository.save(new InventoryItem(null, "FOOD-001", "Basmati Rice", "FOOD", "Premium Sri Lankan Basmati Rice", 150.0, "KG", 20.0, 300.0, 180.0, "Keells Wholesale", "+94-11-2345678", null, "Dry Storage Room", null, null, null, null));
            repository.save(new InventoryItem(null, "FOOD-002", "Red Rice", "FOOD", "Organic Sri Lankan Red Rice", 75.0, "KG", 15.0, 150.0, 95.0, "Local Farmers Co-op", "+94-81-2234567", null, "Dry Storage Room", null, null, null, null));
            repository.save(new InventoryItem(null, "FOOD-003", "Ceylon Tea", "BEVERAGE", "Pure Ceylon Black Tea", 35.0, "KG", 5.0, 50.0, 28.0, "Ceylon Tea Traders", "+94-52-2223344", null, "Pantry", null, null, null, null));
            repository.save(new InventoryItem(null, "FOOD-004", "Coconut Oil", "FOOD", "Extra Virgin Coconut Oil", 45.0, "LITERS", 10.0, 80.0, 52.0, "CIC Holdings", "+94-11-4567890", null, "Kitchen Storage", null, null, null, null));
            
            // Beverages
            repository.save(new InventoryItem(null, "BEV-001", "King Coconut", "BEVERAGE", "Fresh King Coconuts (Thambili)", 120.0, "PIECES", 30.0, 200.0, 145.0, "Pol Gaha Suppliers", "+94-77-1234567", null, "Cold Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "BEV-002", "Fresh Lime", "BEVERAGE", "Sri Lankan Green Limes", 25.0, "KG", 5.0, 40.0, 18.0, "Fresh Fruits Lanka", "+94-33-2223344", null, "Cold Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "BEV-003", "Ginger", "BEVERAGE", "Fresh Sri Lankan Ginger", 18.0, "KG", 5.0, 30.0, 22.0, "Spice Garden", "+94-81-3334455", null, "Cold Storage", null, null, null, null));
            
            // Cleaning Supplies
            repository.save(new InventoryItem(null, "CLEAN-001", "Floor Cleaner", "CLEANING", "Multi-surface floor cleaning liquid", 28.0, "LITERS", 10.0, 50.0, 35.0, "Abans Cleaning", "+94-11-5556677", null, "Cleaning Supplies Room", null, null, null, null));
            repository.save(new InventoryItem(null, "CLEAN-002", "Toilet Cleaner", "CLEANING", "Bathroom cleaning solution", 22.0, "LITERS", 10.0, 40.0, 8.0, "Abans Cleaning", "+94-11-5556677", null, "Cleaning Supplies Room", null, null, null, null));
            repository.save(new InventoryItem(null, "CLEAN-003", "Disinfectant", "CLEANING", "Hospital-grade disinfectant", 35.0, "LITERS", 15.0, 60.0, 42.0, "Dettol Lanka", "+94-11-7778899", null, "Cleaning Supplies Room", null, null, null, null));
            
            // Linen
            repository.save(new InventoryItem(null, "LINEN-001", "Bed Sheets", "LINEN", "Premium Cotton Bed Sheets (King)", 180.0, "PIECES", 30.0, 250.0, 195.0, "Hayleys Textiles", "+94-11-2344556", null, "Linen Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "LINEN-002", "Bath Towels", "LINEN", "White Cotton Bath Towels", 220.0, "PIECES", 50.0, 300.0, 245.0, "Hayleys Textiles", "+94-11-2344556", null, "Linen Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "LINEN-003", "Pillow Cases", "LINEN", "Soft Cotton Pillow Cases", 95.0, "PIECES", 20.0, 150.0, 88.0, "Hayleys Textiles", "+94-11-2344556", null, "Linen Storage", null, null, null, null));
            
            // Amenities
            repository.save(new InventoryItem(null, "AMEN-001", "Shampoo", "AMENITIES", "Herbal Shampoo Bottles (50ml)", 145.0, "PIECES", 30.0, 250.0, 155.0, "Spa Ceylon", "+94-11-4445566", null, "Housekeeping Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "AMEN-002", "Soap Bars", "AMENITIES", "Ayurvedic Herbal Soap Bars", 280.0, "PIECES", 50.0, 400.0, 315.0, "Spa Ceylon", "+94-11-4445566", null, "Housekeeping Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "AMEN-003", "Toothbrush Kit", "AMENITIES", "Complimentary Toothbrush & Paste", 190.0, "PIECES", 40.0, 300.0, 198.0, "Unilever Lanka", "+94-11-9998877", null, "Housekeeping Storage", null, null, null, null));
            repository.save(new InventoryItem(null, "AMEN-004", "Slippers", "AMENITIES", "Disposable Room Slippers", 165.0, "PAIRS", 35.0, 250.0, 4.0, "Comfort Products", "+94-77-3334455", null, "Housekeeping Storage", null, null, null, null));
            
            System.out.println("✅ Initialized 17 Sri Lankan inventory items");
        }
    }
}
