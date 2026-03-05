package com.nsbm.group03.eventManagementService.repository;

import com.nsbm.group03.eventManagementService.entity.Event;
import com.nsbm.group03.eventManagementService.entity.EventStatus;
import com.nsbm.group03.eventManagementService.entity.EventType;
import com.nsbm.group03.eventManagementService.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventCode(String eventCode);
    List<Event> findByEventType(EventType eventType);
    List<Event> findByVenue(Venue venue);
    List<Event> findByStatus(EventStatus status);
    List<Event> findByOrganizerEmail(String organizerEmail);
    
    @Query("SELECT e FROM Event e WHERE e.startDate >= CURRENT_DATE AND e.status != 'CANCELLED' AND e.status != 'COMPLETED' ORDER BY e.startDate")
    List<Event> findUpcomingEvents();
    
    @Query("SELECT e FROM Event e WHERE e.startDate BETWEEN :startDate AND :endDate")
    List<Event> findEventsBetweenDates(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e FROM Event e WHERE e.venue = :venue AND e.startDate = :date AND e.status != 'CANCELLED'")
    List<Event> findVenueBookingsForDate(Venue venue, LocalDate date);
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.startDate >= CURRENT_DATE AND e.status = 'CONFIRMED'")
    Long countConfirmedUpcomingEvents();
    
    @Query("SELECT SUM(e.estimatedCost) FROM Event e WHERE e.status != 'CANCELLED'")
    Double totalProjectedRevenue();
}
