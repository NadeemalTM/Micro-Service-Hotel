package com.nsbm.group03.eventManagementService.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String eventCode;
    private String eventName;
    private String eventType;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer attendeeCount;
    private String organizerName;
    private String organizerEmail;
    private String organizerPhone;
    private String status;
    private Boolean cateringRequired;
    private String cateringDetails;
    private String equipmentNeeded;
    private String specialRequirements;
    private Double estimatedCost;
    private Double actualCost;
    private Double depositPaid;
    private Double balanceDue;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
