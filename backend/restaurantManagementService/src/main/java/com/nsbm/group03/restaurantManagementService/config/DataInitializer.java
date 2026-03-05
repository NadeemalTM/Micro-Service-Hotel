package com.nsbm.group03.restaurantManagementService.config;

import com.nsbm.group03.restaurantManagementService.entity.*;
import com.nsbm.group03.restaurantManagementService.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantOrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        if (menuItemRepository.count() == 0) {
            initializeMenuItems();
        }
        if (orderRepository.count() == 0) {
            initializeOrders();
        }
    }

    private void initializeMenuItems() {
        // Sri Lankan Appetizers
        menuItemRepository.save(new MenuItem(null, "Pol Sambol", "Traditional coconut relish with chili and lime", "APPETIZER", 450.00, "SRI_LANKAN", true, true, true, false, true, 5, 120, null, "Coconut, Red Onion, Chili, Lime, Salt", 92));
        menuItemRepository.save(new MenuItem(null, "Isso Wade", "Crispy prawn fritters with spices", "APPETIZER", 1250.00, "SRI_LANKAN", true, false, false, false, true, 12, 280, null, "Prawns, Lentils, Curry Leaves, Spices", 88));
        menuItemRepository.save(new MenuItem(null, "Cutlets", "Golden fried spiced fish or chicken cutlets", "APPETIZER", 850.00, "SRI_LANKAN", true, false, false, false, false, 10, 220, null, "Fish/Chicken, Potatoes, Breadcrumbs, Spices", 85));
        menuItemRepository.save(new MenuItem(null, "Parippu Wade", "Spicy lentil fritters", "APPETIZER", 650.00, "SRI_LANKAN", true, true, true, false, true, 8, 180, null, "Lentils, Onion, Curry Leaves, Chili", 90));
        
        // Sri Lankan Main Course
        menuItemRepository.save(new MenuItem(null, "Rice & Curry (Fish)", "Traditional rice with fish curry and sides", "MAIN_COURSE", 1850.00, "SRI_LANKAN", true, false, false, false, true, 20, 550, null, "Rice, Fish Curry, Dhal, Vegetables, Sambol", 95));
        menuItemRepository.save(new MenuItem(null, "Chicken Curry with Rice", "Spicy Sri Lankan chicken curry with coconut milk", "MAIN_COURSE", 1650.00, "SRI_LANKAN", true, false, false, false, true, 25, 620, null, "Chicken, Coconut Milk, Rice, Spices", 93));
        menuItemRepository.save(new MenuItem(null, "Lamprais", "Dutch burgher rice dish baked in banana leaf", "MAIN_COURSE", 1950.00, "SRI_LANKAN", true, false, false, false, false, 30, 680, null, "Rice, Beef, Egg, Frikkadel, Banana Leaf", 89));
        menuItemRepository.save(new MenuItem(null, "Kottu Roti", "Chopped roti stir-fried with vegetables and egg", "MAIN_COURSE", 1450.00, "SRI_LANKAN", true, false, false, false, true, 15, 580, null, "Roti, Vegetables, Egg, Curry, Spices", 96));
        menuItemRepository.save(new MenuItem(null, "Seafood Platter", "Fresh catch from Indian Ocean - prawns, fish, crab", "MAIN_COURSE", 3850.00, "SRI_LANKAN", true, false, false, false, false, 35, 720, null, "Prawns, Fish, Crab, Vegetables, Spices", 91));
        menuItemRepository.save(new MenuItem(null, "Vegetable Kottu", "Vegetarian kottu with fresh vegetables", "MAIN_COURSE", 1250.00, "SRI_LANKAN", true, true, true, false, true, 15, 480, null, "Roti, Mixed Vegetables, Curry", 87));
        
        // Sri Lankan Desserts
        menuItemRepository.save(new MenuItem(null, "Watalappan", "Traditional coconut custard pudding", "DESSERT", 650.00, "SRI_LANKAN", true, true, false, false, false, 5, 320, null, "Coconut Milk, Jaggery, Eggs, Cashews, Cardamom", 94));
        menuItemRepository.save(new MenuItem(null, "Curd & Treacle", "Buffalo curd with kithul treacle", "DESSERT", 550.00, "SRI_LANKAN", true, true, false, false, false, 3, 180, null, "Buffalo Curd, Kithul Treacle", 92));
        menuItemRepository.save(new MenuItem(null, "Kavum", "Deep fried sweet oil cakes", "DESSERT", 450.00, "SRI_LANKAN", true, true, false, false, false, 8, 280, null, "Rice Flour, Treacle, Coconut", 85));
        
        // Sri Lankan Beverages
        menuItemRepository.save(new MenuItem(null, "Ceylon Tea", "Pure Ceylon black tea from hill country", "BEVERAGE", 350.00, "SRI_LANKAN", true, true, true, true, false, 3, 5, null, "Ceylon Tea Leaves", 98));
        menuItemRepository.save(new MenuItem(null, "King Coconut Water", "Fresh thambili (king coconut) water", "BEVERAGE", 450.00, "SRI_LANKAN", true, true, true, true, false, 2, 60, null, "King Coconut", 95));
        menuItemRepository.save(new MenuItem(null, "Ginger Tea", "Spicy Sri Lankan ginger tea", "BEVERAGE", 380.00, "SRI_LANKAN", true, true, true, false, false, 5, 40, null, "Ginger, Tea, Jaggery", 88));
        menuItemRepository.save(new MenuItem(null, "Faluda", "Traditional rose-flavored dessert drink", "BEVERAGE", 550.00, "SRI_LANKAN", true, true, false, false, false, 5, 320, null, "Milk, Rose Syrup, Jelly, Ice Cream", 90));
        menuItemRepository.save(new MenuItem(null, "Fresh Lime Juice", "Refreshing lime juice with mint", "BEVERAGE", 420.00, "SRI_LANKAN", true, true, true, true, false, 3, 80, null, "Lime, Mint, Sugar", 86));

        System.out.println("✅ Initialized 18 Sri Lankan menu items");
    }

    private void initializeOrders() {
        RestaurantOrder order1 = new RestaurantOrder();
        order1.setOrderNumber("ORD-1001");
        order1.setTableNumber(5);
        order1.setGuestName("Kasun Perera");
        order1.setOrderType(OrderType.DINE_IN);
        order1.setStatus(OrderStatus.PREPARING);
        order1.setPaymentStatus(PaymentStatus.UNPAID);
        order1.setSubtotal(4650.00);
        order1.setTaxAmount(465.00);
        order1.setServiceCharge(232.50);
        order1.setTotalAmount(5347.50);
        order1.setDiscountAmount(0.0);
        order1.setFinalAmount(5347.50);
        order1.setGuestCount(2);
        order1.setServerName("Hashini");
        order1.setOrderTime(LocalDateTime.now().minusMinutes(15));
        orderRepository.save(order1);

        RestaurantOrder order2 = new RestaurantOrder();
        order2.setOrderNumber("ORD-1002");
        order2.setTableNumber(3);
        order2.setGuestName("Sanduni Fernando");
        order2.setOrderType(OrderType.DINE_IN);
        order2.setStatus(OrderStatus.COMPLETED);
        order2.setPaymentStatus(PaymentStatus.PAID);
        order2.setPaymentMethod("CREDIT_CARD");
        order2.setSubtotal(6850.00);
        order2.setTaxAmount(685.00);
        order2.setServiceCharge(342.50);
        order2.setTotalAmount(7877.50);
        order2.setDiscountAmount(500.0);
        order2.setFinalAmount(7377.50);
        order2.setGuestCount(4);
        order2.setServerName("Buddhika");
        order2.setOrderTime(LocalDateTime.now().minusHours(1));
        order2.setCompletedTime(LocalDateTime.now().minusMinutes(20));
        orderRepository.save(order2);

        RestaurantOrder order3 = new RestaurantOrder();
        order3.setOrderNumber("ORD-1003");
        order3.setRoomNumber(301L);
        order3.setGuestName("Aruna Wickremesinghe");
        order3.setOrderType(OrderType.ROOM_SERVICE);
        order3.setStatus(OrderStatus.READY);
        order3.setPaymentStatus(PaymentStatus.UNPAID);
        order3.setSubtotal(3250.00);
        order3.setTaxAmount(3.30);
        order3.setServiceCharge(1.65);
        order3.setTotalAmount(37.93);
        order3.setDiscountAmount(0.0);
        order3.setFinalAmount(37.93);
        order3.setGuestCount(1);
        order3.setOrderTime(LocalDateTime.now().minusMinutes(25));
        order3.setPreparedTime(LocalDateTime.now().minusMinutes(5));
        orderRepository.save(order3);

        System.out.println("✅ Initialized 3 sample restaurant orders");
    }
}
