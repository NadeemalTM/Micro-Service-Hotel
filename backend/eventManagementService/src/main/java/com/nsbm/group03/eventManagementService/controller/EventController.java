package com.nsbm.group03.eventManagementService.controller;

import com.nsbm.group03.eventManagementService.dto.EventDTO;
import com.nsbm.group03.eventManagementService.response.ApiResponse;
import com.nsbm.group03.eventManagementService.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAllEvents() {
        return ResponseEntity.ok(ApiResponse.success("Events retrieved", service.getAllEvents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Event retrieved", service.getEventById(id)));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getUpcomingEvents() {
        return ResponseEntity.ok(ApiResponse.success("Upcoming events retrieved", service.getUpcomingEvents()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(ApiResponse.success("Events by type retrieved", service.getEventsByType(type)));
    }

    @GetMapping("/venue/{venue}")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getByVenue(@PathVariable String venue) {
        return ResponseEntity.ok(ApiResponse.success("Events by venue retrieved", service.getEventsByVenue(venue)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDTO>> createEvent(@RequestBody EventDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Event created", service.createEvent(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> updateEvent(@PathVariable Long id, @RequestBody EventDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Event updated", service.updateEvent(id, dto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EventDTO>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", service.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted", null));
    }
}
