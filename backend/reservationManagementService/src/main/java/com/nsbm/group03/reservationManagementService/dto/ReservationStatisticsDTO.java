package com.nsbm.group03.reservationManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatisticsDTO {
    private Long totalReservations;
    private Long currentGuests;
    private Long upcomingCheckIns;
    private Long upcomingCheckOuts;
    private Long confirmedReservations;
    private Long pendingReservations;
    private Long cancelledReservations;
    private Long pendingPayments;
    private Double totalRevenue;
    private Double averageReservationValue;
    private Double occupancyRate;
}
