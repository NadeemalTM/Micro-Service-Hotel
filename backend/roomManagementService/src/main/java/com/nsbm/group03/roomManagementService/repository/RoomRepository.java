package com.nsbm.group03.roomManagementService.repository;

import com.nsbm.group03.roomManagementService.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByRoomNumber(String roomNumber);
    
    List<Room> findByFloor(Integer floor);
    
    List<Room> findByRoomType(String roomType);
    
    List<Room> findByStatus(String status);
    
    List<Room> findByRoomTypeAndStatus(String roomType, String status);
    
    List<Room> findByPricePerNightLessThanEqual(Double maxPrice);
    
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);
    
    boolean existsByRoomNumber(String roomNumber);
}
