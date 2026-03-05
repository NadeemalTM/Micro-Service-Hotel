package com.nsbm.group03.roomManagementService.service;

import com.nsbm.group03.roomManagementService.dto.RoomDTO;
import com.nsbm.group03.roomManagementService.dto.RoomStatisticsDTO;
import com.nsbm.group03.roomManagementService.entity.Room;
import com.nsbm.group03.roomManagementService.exception.DuplicateResourceException;
import com.nsbm.group03.roomManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.roomManagementService.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return convertToDTO(room);
    }
    
    public RoomDTO getRoomByRoomNumber(String roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with number: " + roomNumber));
        return convertToDTO(room);
    }
    
    public List<RoomDTO> getRoomsByFloor(Integer floor) {
        return roomRepository.findByFloor(floor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RoomDTO> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType(roomType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RoomDTO> getRoomsByStatus(String status) {
        return roomRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RoomDTO> getAvailableRoomsByType(String roomType) {
        return roomRepository.findByRoomTypeAndStatus(roomType, "AVAILABLE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RoomDTO createRoom(RoomDTO roomDTO) {
        if (roomRepository.existsByRoomNumber(roomDTO.getRoomNumber())) {
            throw new DuplicateResourceException("Room already exists with number: " + roomDTO.getRoomNumber());
        }
        
        Room room = convertToEntity(roomDTO);
        room.setCreatedAt(LocalDateTime.now());
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }
    
    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        
        // Check if room number is being changed and if it already exists
        if (!room.getRoomNumber().equals(roomDTO.getRoomNumber()) && 
            roomRepository.existsByRoomNumber(roomDTO.getRoomNumber())) {
            throw new DuplicateResourceException("Room already exists with number: " + roomDTO.getRoomNumber());
        }
        
        updateRoomFromDTO(room, roomDTO);
        room.setUpdatedAt(LocalDateTime.now());
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }
    
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        roomRepository.delete(room);
    }
    
    public RoomDTO updateRoomStatus(Long id, String status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setStatus(status);
        room.setUpdatedAt(LocalDateTime.now());
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }
    
    public RoomDTO markRoomCleaned(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setLastCleaned(LocalDateTime.now());
        room.setStatus("AVAILABLE");
        room.setUpdatedAt(LocalDateTime.now());
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }
    
    public RoomDTO markRoomMaintenance(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setLastMaintenance(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }
    
    public RoomStatisticsDTO getStatistics() {
        long totalRooms = roomRepository.count();
        long availableRooms = roomRepository.findByStatus("AVAILABLE").size();
        long occupiedRooms = roomRepository.findByStatus("OCCUPIED").size();
        long maintenanceRooms = roomRepository.findByStatus("MAINTENANCE").size();
        long reservedRooms = roomRepository.findByStatus("RESERVED").size();
        
        double averagePrice = roomRepository.findAll().stream()
                .mapToDouble(Room::getPricePerNight)
                .average()
                .orElse(0.0);
        
        return new RoomStatisticsDTO(totalRooms, availableRooms, occupiedRooms, 
                                     maintenanceRooms, reservedRooms, averagePrice);
    }
    
    // Helper methods
    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setRoomType(room.getRoomType());
        dto.setStatus(room.getStatus());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setBedCount(room.getBedCount());
        dto.setBedType(room.getBedType());
        dto.setHasBalcony(room.getHasBalcony());
        dto.setHasSeaView(room.getHasSeaView());
        dto.setRoomSize(room.getRoomSize());
        dto.setAmenities(room.getAmenities());
        dto.setDescription(room.getDescription());
        return dto;
    }
    
    private Room convertToEntity(RoomDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setRoomType(dto.getRoomType());
        room.setStatus(dto.getStatus());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCapacity(dto.getCapacity());
        room.setBedCount(dto.getBedCount());
        room.setBedType(dto.getBedType());
        room.setHasBalcony(dto.getHasBalcony());
        room.setHasSeaView(dto.getHasSeaView());
        room.setRoomSize(dto.getRoomSize());
        room.setAmenities(dto.getAmenities());
        room.setDescription(dto.getDescription());
        return room;
    }
    
    private void updateRoomFromDTO(Room room, RoomDTO dto) {
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setRoomType(dto.getRoomType());
        room.setStatus(dto.getStatus());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCapacity(dto.getCapacity());
        room.setBedCount(dto.getBedCount());
        room.setBedType(dto.getBedType());
        room.setHasBalcony(dto.getHasBalcony());
        room.setHasSeaView(dto.getHasSeaView());
        room.setRoomSize(dto.getRoomSize());
        room.setAmenities(dto.getAmenities());
        room.setDescription(dto.getDescription());
    }
}
