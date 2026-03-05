package com.nsbm.group03.eventManagementService.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String eventCode;
    
    @Column(nullable = false)
    private String eventName;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Venue venue;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column
    private LocalTime startTime;
    
    @Column
    private LocalTime endTime;
    
    @Column(nullable = false)
    private Integer attendeeCount;
    
    @Column(nullable = false)
    private String organizerName;
    
    @Column(nullable = false)
    private String organizerEmail;
    
    @Column(nullable = false)
    private String organizerPhone;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    
    @Column
    private Boolean cateringRequired;
    
    @Column(length = 1000)
    private String cateringDetails;
    
    @Column(length = 1000)
    private String equipmentNeeded;
    
    @Column(length = 1000)
    private String specialRequirements;
    
    @Column
    private Double estimatedCost;
    
    @Column
    private Double actualCost;
    
    @Column
    private Double depositPaid;
    
    @Column
    private Double balanceDue;
    
    @Column(length = 1000)
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = EventStatus.PLANNED;
        }
        if (depositPaid == null) {
            depositPaid = 0.0;
        }
        calculateBalance();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateBalance();
    }
    
    private void calculateBalance() {
        if (estimatedCost != null && depositPaid != null) {
            balanceDue = estimatedCost - depositPaid;
        }
    }
}
