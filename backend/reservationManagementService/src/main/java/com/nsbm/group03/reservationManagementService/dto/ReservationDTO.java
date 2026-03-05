package com.nsbm.group03.reservationManagementService.dto;

import com.nsbm.group03.reservationManagementService.entity.ReservationStatus;
import com.nsbm.group03.reservationManagementService.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private Long roomId;
    private String roomNumber;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
    private String identificationType;
    private String identificationNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private ReservationStatus status;
    private Double pricePerNight;
    private Double totalAmount;
    private Double discountAmount;
    private Double finalAmount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private String specialRequests;
    private Boolean breakfastIncluded;
    private Boolean parkingRequired;
    private Boolean airportPickup;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String bookedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer numberOfNights;
}
