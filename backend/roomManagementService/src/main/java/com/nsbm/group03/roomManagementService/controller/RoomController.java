package com.nsbm.group03.roomManagementService.controller;

import com.nsbm.group03.roomManagementService.dto.RoomDTO;
import com.nsbm.group03.roomManagementService.dto.RoomStatisticsDTO;
import com.nsbm.group03.roomManagementService.response.ApiResponse;
import com.nsbm.group03.roomManagementService.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.success("Room retrieved successfully", room));
    }
    
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomByRoomNumber(@PathVariable String roomNumber) {
        RoomDTO room = roomService.getRoomByRoomNumber(roomNumber);
        return ResponseEntity.ok(ApiResponse.success("Room retrieved successfully", room));
    }
    
    @GetMapping("/floor/{floor}")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getRoomsByFloor(@PathVariable Integer floor) {
        List<RoomDTO> rooms = roomService.getRoomsByFloor(floor);
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }
    
    @GetMapping("/type/{roomType}")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getRoomsByType(@PathVariable String roomType) {
        List<RoomDTO> rooms = roomService.getRoomsByType(roomType);
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getRoomsByStatus(@PathVariable String status) {
        List<RoomDTO> rooms = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }
    
    @GetMapping("/available/{roomType}")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAvailableRoomsByType(@PathVariable String roomType) {
        List<RoomDTO> rooms = roomService.getAvailableRoomsByType(roomType);
        return ResponseEntity.ok(ApiResponse.success("Available rooms retrieved successfully", rooms));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<RoomDTO>> createRoom(@RequestBody RoomDTO roomDTO) {
        RoomDTO createdRoom = roomService.createRoom(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Room created successfully", createdRoom));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomDTO>> updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDTO) {
        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(ApiResponse.success("Room updated successfully", updatedRoom));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room deleted successfully", null));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RoomDTO>> updateRoomStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        RoomDTO updatedRoom = roomService.updateRoomStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Room status updated successfully", updatedRoom));
    }
    
    @PatchMapping("/{id}/clean")
    public ResponseEntity<ApiResponse<RoomDTO>> markRoomCleaned(@PathVariable Long id) {
        RoomDTO updatedRoom = roomService.markRoomCleaned(id);
        return ResponseEntity.ok(ApiResponse.success("Room marked as cleaned", updatedRoom));
    }
    
    @PatchMapping("/{id}/maintenance")
    public ResponseEntity<ApiResponse<RoomDTO>> markRoomMaintenance(@PathVariable Long id) {
        RoomDTO updatedRoom = roomService.markRoomMaintenance(id);
        return ResponseEntity.ok(ApiResponse.success("Room maintenance recorded", updatedRoom));
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<RoomStatisticsDTO>> getStatistics() {
        RoomStatisticsDTO statistics = roomService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", statistics));
    }
}
