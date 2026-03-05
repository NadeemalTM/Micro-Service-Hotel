package com.nsbm.group03.kitchenManagementService.config;

import com.nsbm.group03.kitchenManagementService.entity.KitchenOrder;
import com.nsbm.group03.kitchenManagementService.entity.KitchenStatus;
import com.nsbm.group03.kitchenManagementService.repository.KitchenOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private KitchenOrderRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            KitchenOrder o1 = new KitchenOrder();
            o1.setOrderNumber("KO-1001");
            o1.setRestaurantOrderId(1001L);
            o1.setTableNumber(5);
            o1.setOrderType("DINE_IN");
            o1.setItems("Rice & Curry (Fish), Kottu Roti, Ceylon Tea");
            o1.setStatus(KitchenStatus.PREPARING);
            o1.setAssignedChef("Chef Roshan");
            o1.setPriority(1);
            o1.setEstimatedTime(25);
            o1.setSpecialInstructions("Extra spicy, no onions");
            repository.save(o1);

            KitchenOrder o2 = new KitchenOrder();
            o2.setOrderNumber("KO-1002");
            o2.setRestaurantOrderId(1002L);
            o2.setRoomNumber(301L);
            o2.setOrderType("ROOM_SERVICE");
            o2.setItems("Chicken Curry with Rice, Watalappan, King Coconut Water");
            o2.setStatus(KitchenStatus.PENDING);
            o2.setAssignedChef("Chef Dilini");
            o2.setPriority(1);
            o2.setEstimatedTime(30);
            o2.setSpecialInstructions("Mild spice level");
            repository.save(o2);
            
            KitchenOrder o3 = new KitchenOrder();
            o3.setOrderNumber("KO-1003");
            o3.setRestaurantOrderId(1003L);
            o3.setTableNumber(12);
            o3.setOrderType("DINE_IN");
            o3.setItems("Lamprais, Isso Wade, Fresh Lime Juice");
            o3.setStatus(KitchenStatus.READY);
            o3.setAssignedChef("Chef Nuwan");
            o3.setPriority(2);
            o3.setEstimatedTime(35);
            repository.save(o3);

            System.out.println("✅ Initialized 3 Sri Lankan kitchen orders");
        }
    }
}
