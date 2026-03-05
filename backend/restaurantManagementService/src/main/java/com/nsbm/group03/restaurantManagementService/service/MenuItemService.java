package com.nsbm.group03.restaurantManagementService.service;

import com.nsbm.group03.restaurantManagementService.dto.MenuItemDTO;
import com.nsbm.group03.restaurantManagementService.entity.MenuItem;
import com.nsbm.group03.restaurantManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.restaurantManagementService.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getAvailableMenuItems() {
        return menuItemRepository.findByAvailable(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return convertToDTO(menuItem);
    }

    public List<MenuItemDTO> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO createMenuItem(MenuItemDTO dto) {
        MenuItem menuItem = convertToEntity(dto);
        MenuItem saved = menuItemRepository.save(menuItem);
        return convertToDTO(saved);
    }

    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO dto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        updateMenuItemFromDTO(menuItem, dto);
        MenuItem updated = menuItemRepository.save(menuItem);
        return convertToDTO(updated);
    }

    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        menuItemRepository.delete(menuItem);
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setCategory(menuItem.getCategory());
        dto.setPrice(menuItem.getPrice());
        dto.setCuisine(menuItem.getCuisine());
        dto.setAvailable(menuItem.getAvailable());
        dto.setVegetarian(menuItem.getVegetarian());
        dto.setVegan(menuItem.getVegan());
        dto.setGlutenFree(menuItem.getGlutenFree());
        dto.setSpicy(menuItem.getSpicy());
        dto.setPreparationTime(menuItem.getPreparationTime());
        dto.setCalories(menuItem.getCalories());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setIngredients(menuItem.getIngredients());
        dto.setPopularityScore(menuItem.getPopularityScore());
        return dto;
    }

    private MenuItem convertToEntity(MenuItemDTO dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setCategory(dto.getCategory());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCuisine(dto.getCuisine());
        menuItem.setAvailable(dto.getAvailable());
        menuItem.setVegetarian(dto.getVegetarian());
        menuItem.setVegan(dto.getVegan());
        menuItem.setGlutenFree(dto.getGlutenFree());
        menuItem.setSpicy(dto.getSpicy());
        menuItem.setPreparationTime(dto.getPreparationTime());
        menuItem.setCalories(dto.getCalories());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setIngredients(dto.getIngredients());
        menuItem.setPopularityScore(dto.getPopularityScore());
        return menuItem;
    }

    private void updateMenuItemFromDTO(MenuItem menuItem, MenuItemDTO dto) {
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setCategory(dto.getCategory());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCuisine(dto.getCuisine());
        menuItem.setAvailable(dto.getAvailable());
        menuItem.setVegetarian(dto.getVegetarian());
        menuItem.setVegan(dto.getVegan());
        menuItem.setGlutenFree(dto.getGlutenFree());
        menuItem.setSpicy(dto.getSpicy());
        menuItem.setPreparationTime(dto.getPreparationTime());
        menuItem.setCalories(dto.getCalories());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setIngredients(dto.getIngredients());
        menuItem.setPopularityScore(dto.getPopularityScore());
    }
}
