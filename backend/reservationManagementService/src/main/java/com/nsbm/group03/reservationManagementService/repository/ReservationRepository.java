package com.nsbm.group03.reservationManagementService.repository;

import com.nsbm.group03.reservationManagementService.entity.Reservation;
import com.nsbm.group03.reservationManagementService.entity.ReservationStatus;
import com.nsbm.group03.reservationManagementService.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find by status
    List<Reservation> findByStatus(ReservationStatus status);
    
    // Find by room
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByRoomNumber(String roomNumber);
    
    // Find by guest
    List<Reservation> findByGuestEmail(String email);
    List<Reservation> findByGuestPhone(String phone);
    Optional<Reservation> findByGuestEmailAndStatus(String email, ReservationStatus status);
    
    // Find by date
    List<Reservation> findByCheckInDate(LocalDate date);
    List<Reservation> findByCheckOutDate(LocalDate date);
    List<Reservation> findByCheckInDateBetween(LocalDate start, LocalDate end);
    List<Reservation> findByCheckOutDateBetween(LocalDate start, LocalDate end);
    
    // Find upcoming check-ins
    @Query("SELECT r FROM Reservation r WHERE r.checkInDate = :date AND r.status = 'CONFIRMED'")
    List<Reservation> findUpcomingCheckIns(@Param("date") LocalDate date);
    
    // Find upcoming check-outs
    @Query("SELECT r FROM Reservation r WHERE r.checkOutDate = :date AND r.status = 'CHECKED_IN'")
    List<Reservation> findUpcomingCheckOuts(@Param("date") LocalDate date);
    
    // Find current guests (checked in)
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CHECKED_IN'")
    List<Reservation> findCurrentGuests();
    
    // Find by payment status
    List<Reservation> findByPaymentStatus(PaymentStatus paymentStatus);
    
    // Find pending payments
    @Query("SELECT r FROM Reservation r WHERE r.paymentStatus IN ('PENDING', 'PARTIAL_PAID')")
    List<Reservation> findPendingPayments();
    
    // Check room availability
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId " +
           "AND r.status NOT IN ('CANCELLED', 'CHECKED_OUT', 'NO_SHOW') " +
           "AND ((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    List<Reservation> findConflictingReservations(
        @Param("roomId") Long roomId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );
    
    // Find by booked by employee
    List<Reservation> findByBookedBy(String bookedBy);
    
    // Statistics queries
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = 'CHECKED_IN'")
    Long countCurrentGuests();
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.checkInDate = :date AND r.status = 'CONFIRMED'")
    Long countCheckInsForDate(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.checkOutDate = :date AND r.status = 'CHECKED_IN'")
    Long countCheckOutsForDate(@Param("date") LocalDate date);
    
    @Query("SELECT SUM(r.finalAmount) FROM Reservation r WHERE r.createdAt >= :startDate AND r.status != 'CANCELLED'")
    Double totalRevenueFrom(@Param("startDate") LocalDateTime startDate);
}
