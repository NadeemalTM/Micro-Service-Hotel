package com.nsbm.group03.reservationManagementService.controller;

import com.nsbm.group03.reservationManagementService.dto.ReservationDTO;
import com.nsbm.group03.reservationManagementService.dto.ReservationStatisticsDTO;
import com.nsbm.group03.reservationManagementService.response.ApiResponse;
import com.nsbm.group03.reservationManagementService.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation retrieved successfully", reservation));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getReservationsByStatus(@PathVariable String status) {
        List<ReservationDTO> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getReservationsByRoom(@PathVariable Long roomId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByRoomId(roomId);
        return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
    }

    @GetMapping("/guest/{email}")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getReservationsByGuest(@PathVariable String email) {
        List<ReservationDTO> reservations = reservationService.getReservationsByGuestEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
    }

    @GetMapping("/current-guests")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getCurrentGuests() {
        List<ReservationDTO> guests = reservationService.getCurrentGuests();
        return ResponseEntity.ok(ApiResponse.success("Current guests retrieved successfully", guests));
    }

    @GetMapping("/upcoming-checkins")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getUpcomingCheckIns(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate checkDate = date != null ? date : LocalDate.now();
        List<ReservationDTO> checkIns = reservationService.getUpcomingCheckIns(checkDate);
        return ResponseEntity.ok(ApiResponse.success("Upcoming check-ins retrieved successfully", checkIns));
    }

    @GetMapping("/upcoming-checkouts")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getUpcomingCheckOuts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate checkDate = date != null ? date : LocalDate.now();
        List<ReservationDTO> checkOuts = reservationService.getUpcomingCheckOuts(checkDate);
        return ResponseEntity.ok(ApiResponse.success("Upcoming check-outs retrieved successfully", checkOuts));
    }

    @GetMapping("/pending-payments")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getPendingPayments() {
        List<ReservationDTO> pending = reservationService.getPendingPayments();
        return ResponseEntity.ok(ApiResponse.success("Pending payments retrieved successfully", pending));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<ReservationStatisticsDTO>> getStatistics() {
        ReservationStatisticsDTO stats = reservationService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDTO>> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO created = reservationService.createReservation(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reservation created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDTO>> updateReservation(
            @PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updated = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(ApiResponse.success("Reservation updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully", null));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<ReservationDTO>> confirmReservation(@PathVariable Long id) {
        ReservationDTO confirmed = reservationService.confirmReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation confirmed successfully", confirmed));
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<ReservationDTO>> checkIn(@PathVariable Long id) {
        ReservationDTO checkedIn = reservationService.checkIn(id);
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully", checkedIn));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<ReservationDTO>> checkOut(@PathVariable Long id) {
        ReservationDTO checkedOut = reservationService.checkOut(id);
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully", checkedOut));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ReservationDTO>> cancelReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        ReservationDTO cancelled = reservationService.cancelReservation(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", cancelled));
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<ReservationDTO>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String transactionId) {
        ReservationDTO updated = reservationService.updatePaymentStatus(id, status, method, transactionId);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated successfully", updated));
    }
}
