package com.nsbm.group03.reservationManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long roomId;
    
    @Column(nullable = false)
    private String roomNumber;
    
    @Column(nullable = false)
    private String guestName;
    
    @Column(nullable = false, unique = true)
    private String guestEmail;
    
    @Column(nullable = false)
    private String guestPhone;
    
    @Column
    private String guestAddress;
    
    @Column(nullable = false)
    private String identificationType; // PASSPORT, DRIVING_LICENSE, NATIONAL_ID
    
    @Column(nullable = false)
    private String identificationNumber;
    
    @Column(nullable = false)
    private LocalDate checkInDate;
    
    @Column(nullable = false)
    private LocalDate checkOutDate;
    
    @Column(nullable = false)
    private Integer numberOfGuests;
    
    @Column(nullable = false)
    private Integer numberOfAdults;
    
    @Column(nullable = false)
    private Integer numberOfChildren;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    
    @Column(nullable = false)
    private Double pricePerNight;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column
    private Double discountAmount;
    
    @Column
    private Double finalAmount;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    @Column
    private String paymentMethod; // CASH, CREDIT_CARD, DEBIT_CARD, ONLINE
    
    @Column
    private String transactionId;
    
    @Column(length = 1000)
    private String specialRequests;
    
    @Column
    private Boolean breakfastIncluded;
    
    @Column
    private Boolean parkingRequired;
    
    @Column
    private Boolean airportPickup;
    
    @Column
    private LocalDateTime checkInTime;
    
    @Column
    private LocalDateTime checkOutTime;
    
    @Column
    private String bookedBy; // Employee username who created booking
    
    @Column
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime cancelledAt;
    
    @Column
    private String cancellationReason;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = ReservationStatus.PENDING;
        }
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
        if (breakfastIncluded == null) {
            breakfastIncluded = false;
        }
        if (parkingRequired == null) {
            parkingRequired = false;
        }
        if (airportPickup == null) {
            airportPickup = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
