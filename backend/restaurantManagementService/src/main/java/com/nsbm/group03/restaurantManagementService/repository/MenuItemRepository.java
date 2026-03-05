package com.nsbm.group03.restaurantManagementService.repository;

import com.nsbm.group03.restaurantManagementService.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByCategory(String category);
    
    List<MenuItem> findByCuisine(String cuisine);
    
    List<MenuItem> findByAvailable(Boolean available);
    
    List<MenuItem> findByVegetarian(Boolean vegetarian);
    
    List<MenuItem> findByVegan(Boolean vegan);
    
    List<MenuItem> findByGlutenFree(Boolean glutenFree);
    
    List<MenuItem> findByCategoryAndAvailable(String category, Boolean available);
    
    @Query("SELECT m FROM MenuItem m WHERE m.available = true ORDER BY m.popularityScore DESC")
    List<MenuItem> findPopularItems();
    
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MenuItem> searchByName(String keyword);
}
