package com.nsbm.group03.eventManagementService.service;

import com.nsbm.group03.eventManagementService.dto.EventDTO;
import com.nsbm.group03.eventManagementService.entity.Event;
import com.nsbm.group03.eventManagementService.entity.EventStatus;
import com.nsbm.group03.eventManagementService.entity.EventType;
import com.nsbm.group03.eventManagementService.entity.Venue;
import com.nsbm.group03.eventManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.eventManagementService.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository repository;

    public List<EventDTO> getAllEvents() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id)));
    }

    public List<EventDTO> getUpcomingEvents() {
        return repository.findUpcomingEvents().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByType(String type) {
        return repository.findByEventType(EventType.valueOf(type)).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByVenue(String venue) {
        return repository.findByVenue(Venue.valueOf(venue)).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public EventDTO createEvent(EventDTO dto) {
        Event event = new Event();
        updateEntity(event, dto);
        if (dto.getEventCode() == null) {
            event.setEventCode("EVT-" + System.currentTimeMillis());
        }
        return toDTO(repository.save(event));
    }

    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        updateEntity(event, dto);
        return toDTO(repository.save(event));
    }

    public EventDTO updateStatus(Long id, String status) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.setStatus(EventStatus.valueOf(status));
        return toDTO(repository.save(event));
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }

    private void updateEntity(Event event, EventDTO dto) {
        event.setEventCode(dto.getEventCode());
        event.setEventName(dto.getEventName());
        event.setEventType(dto.getEventType() != null ? EventType.valueOf(dto.getEventType()) : null);
        event.setVenue(dto.getVenue() != null ? Venue.valueOf(dto.getVenue()) : null);
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setAttendeeCount(dto.getAttendeeCount());
        event.setOrganizerName(dto.getOrganizerName());
        event.setOrganizerEmail(dto.getOrganizerEmail());
        event.setOrganizerPhone(dto.getOrganizerPhone());
        event.setStatus(dto.getStatus() != null ? EventStatus.valueOf(dto.getStatus()) : null);
        event.setCateringRequired(dto.getCateringRequired());
        event.setCateringDetails(dto.getCateringDetails());
        event.setEquipmentNeeded(dto.getEquipmentNeeded());
        event.setSpecialRequirements(dto.getSpecialRequirements());
        event.setEstimatedCost(dto.getEstimatedCost());
        event.setActualCost(dto.getActualCost());
        event.setDepositPaid(dto.getDepositPaid());
        event.setNotes(dto.getNotes());
    }

    private EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setEventCode(event.getEventCode());
        dto.setEventName(event.getEventName());
        dto.setEventType(event.getEventType() != null ? event.getEventType().name() : null);
        dto.setVenue(event.getVenue() != null ? event.getVenue().name() : null);
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setAttendeeCount(event.getAttendeeCount());
        dto.setOrganizerName(event.getOrganizerName());
        dto.setOrganizerEmail(event.getOrganizerEmail());
        dto.setOrganizerPhone(event.getOrganizerPhone());
        dto.setStatus(event.getStatus() != null ? event.getStatus().name() : null);
        dto.setCateringRequired(event.getCateringRequired());
        dto.setCateringDetails(event.getCateringDetails());
        dto.setEquipmentNeeded(event.getEquipmentNeeded());
        dto.setSpecialRequirements(event.getSpecialRequirements());
        dto.setEstimatedCost(event.getEstimatedCost());
        dto.setActualCost(event.getActualCost());
        dto.setDepositPaid(event.getDepositPaid());
        dto.setBalanceDue(event.getBalanceDue());
        dto.setNotes(event.getNotes());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        return dto;
    }
}
