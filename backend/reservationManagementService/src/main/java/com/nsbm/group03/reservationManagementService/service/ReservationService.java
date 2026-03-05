package com.nsbm.group03.reservationManagementService.service;

import com.nsbm.group03.reservationManagementService.dto.ReservationDTO;
import com.nsbm.group03.reservationManagementService.dto.ReservationStatisticsDTO;
import com.nsbm.group03.reservationManagementService.entity.Reservation;
import com.nsbm.group03.reservationManagementService.entity.ReservationStatus;
import com.nsbm.group03.reservationManagementService.entity.PaymentStatus;
import com.nsbm.group03.reservationManagementService.exception.ResourceNotFoundException;
import com.nsbm.group03.reservationManagementService.exception.DuplicateResourceException;
import com.nsbm.group03.reservationManagementService.exception.InvalidReservationException;
import com.nsbm.group03.reservationManagementService.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return convertToDTO(reservation);
    }

    public List<ReservationDTO> getReservationsByStatus(String status) {
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            return reservationRepository.findByStatus(reservationStatus).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InvalidReservationException("Invalid reservation status: " + status);
        }
    }

    public List<ReservationDTO> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getReservationsByGuestEmail(String email) {
        return reservationRepository.findByGuestEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getCurrentGuests() {
        return reservationRepository.findCurrentGuests().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getUpcomingCheckIns(LocalDate date) {
        return reservationRepository.findUpcomingCheckIns(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getUpcomingCheckOuts(LocalDate date) {
        return reservationRepository.findUpcomingCheckOuts(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getPendingPayments() {
        return reservationRepository.findPendingPayments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
        // Validate dates
        if (dto.getCheckInDate().isAfter(dto.getCheckOutDate())) {
            throw new InvalidReservationException("Check-in date must be before check-out date");
        }
        
        if (dto.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidReservationException("Check-in date cannot be in the past");
        }

        // Validate guest count
        if (dto.getNumberOfAdults() + dto.getNumberOfChildren() != dto.getNumberOfGuests()) {
            throw new InvalidReservationException("Number of guests must equal adults + children");
        }

        // Check for existing reservation with same email and overlapping dates
        List<Reservation> guestReservations = reservationRepository.findByGuestEmail(dto.getGuestEmail());
        for (Reservation existing : guestReservations) {
            if (existing.getStatus() != ReservationStatus.CANCELLED && 
                existing.getStatus() != ReservationStatus.CHECKED_OUT &&
                existing.getStatus() != ReservationStatus.NO_SHOW) {
                if (datesOverlap(dto.getCheckInDate(), dto.getCheckOutDate(), 
                                existing.getCheckInDate(), existing.getCheckOutDate())) {
                    throw new DuplicateResourceException(
                        "Guest already has a reservation during this period");
                }
            }
        }

        // Check room availability
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
            dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
        
        if (!conflicts.isEmpty()) {
            throw new InvalidReservationException("Room is not available for the selected dates");
        }

        Reservation reservation = convertToEntity(dto);
        
        // Calculate amounts
        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        reservation.setTotalAmount(dto.getPricePerNight() * nights);
        
        double discount = dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0.0;
        reservation.setDiscountAmount(discount);
        reservation.setFinalAmount(reservation.getTotalAmount() - discount);

        Reservation saved = reservationRepository.save(reservation);
        return convertToDTO(saved);
    }

    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        // Validate status transitions
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new InvalidReservationException("Cannot update a checked-out reservation");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException("Cannot update a cancelled reservation");
        }

        // If changing dates, check availability
        if (!reservation.getCheckInDate().equals(dto.getCheckInDate()) || 
            !reservation.getCheckOutDate().equals(dto.getCheckOutDate())) {
            
            List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
            
            // Remove current reservation from conflicts
            conflicts.removeIf(r -> r.getId().equals(id));
            
            if (!conflicts.isEmpty()) {
                throw new InvalidReservationException("Room is not available for the selected dates");
            }
        }

        updateReservationFromDTO(reservation, dto);
        
        // Recalculate amounts if dates changed
        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        reservation.setTotalAmount(dto.getPricePerNight() * nights);
        
        double discount = dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0.0;
        reservation.setDiscountAmount(discount);
        reservation.setFinalAmount(reservation.getTotalAmount() - discount);

        Reservation updated = reservationRepository.save(reservation);
        return convertToDTO(updated);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        reservationRepository.delete(reservation);
    }

    public ReservationDTO confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InvalidReservationException("Only pending reservations can be confirmed");
        }
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation updated = reservationRepository.save(reservation);
        return convertToDTO(updated);
    }

    public ReservationDTO checkIn(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new InvalidReservationException("Only confirmed reservations can be checked in");
        }
        
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setCheckInTime(LocalDateTime.now());
        Reservation updated = reservationRepository.save(reservation);
        return convertToDTO(updated);
    }

    public ReservationDTO checkOut(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new InvalidReservationException("Only checked-in reservations can be checked out");
        }
        
        if (reservation.getPaymentStatus() != PaymentStatus.PAID) {
            throw new InvalidReservationException("Payment must be completed before checkout");
        }
        
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setCheckOutTime(LocalDateTime.now());
        Reservation updated = reservationRepository.save(reservation);
        return convertToDTO(updated);
    }

    public ReservationDTO cancelReservation(Long id, String reason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new InvalidReservationException("Cannot cancel a checked-out reservation");
        }
        
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException("Reservation is already cancelled");
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservation.setCancellationReason(reason);
        
        // Handle refund for paid reservations
        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            reservation.setPaymentStatus(PaymentStatus.REFUNDED);
        }
        
        Reservation updated = reservationRepository.save(reservation);
        return convertToDTO(updated);
    }

    public ReservationDTO updatePaymentStatus(Long id, String paymentStatus, String paymentMethod, String transactionId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        try {
            PaymentStatus status = PaymentStatus.valueOf(paymentStatus.toUpperCase());
            reservation.setPaymentStatus(status);
            
            if (paymentMethod != null) {
                reservation.setPaymentMethod(paymentMethod);
            }
            if (transactionId != null) {
                reservation.setTransactionId(transactionId);
            }
            
            Reservation updated = reservationRepository.save(reservation);
            return convertToDTO(updated);
        } catch (IllegalArgumentException e) {
            throw new InvalidReservationException("Invalid payment status: " + paymentStatus);
        }
    }

    public ReservationStatisticsDTO getStatistics() {
        List<Reservation> allReservations = reservationRepository.findAll();
        
        long total = allReservations.size();
        long currentGuests = reservationRepository.countCurrentGuests();
        long confirmedCount = allReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED).count();
        long pendingCount = allReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING).count();
        long cancelledCount = allReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED).count();
        long pendingPayments = reservationRepository.findPendingPayments().size();
        
        LocalDate today = LocalDate.now();
        long upcomingCheckIns = reservationRepository.countCheckInsForDate(today);
        long upcomingCheckOuts = reservationRepository.countCheckOutsForDate(today);
        
        Double totalRevenue = reservationRepository.totalRevenueFrom(LocalDateTime.now().minusMonths(1));
        if (totalRevenue == null) totalRevenue = 0.0;
        
        double averageReservationValue = total > 0 ? 
                allReservations.stream()
                        .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                        .mapToDouble(Reservation::getFinalAmount)
                        .average()
                        .orElse(0.0) : 0.0;
        
        // Calculate occupancy rate (simplified - based on current guests vs total capacity)
        double occupancyRate = (currentGuests / Math.max(1.0, total)) * 100;
        
        return new ReservationStatisticsDTO(
                total,
                currentGuests,
                upcomingCheckIns,
                upcomingCheckOuts,
                confirmedCount,
                pendingCount,
                cancelledCount,
                pendingPayments,
                totalRevenue,
                averageReservationValue,
                occupancyRate
        );
    }

    // Helper methods
    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setRoomId(reservation.getRoomId());
        dto.setRoomNumber(reservation.getRoomNumber());
        dto.setGuestName(reservation.getGuestName());
        dto.setGuestEmail(reservation.getGuestEmail());
        dto.setGuestPhone(reservation.getGuestPhone());
        dto.setGuestAddress(reservation.getGuestAddress());
        dto.setIdentificationType(reservation.getIdentificationType());
        dto.setIdentificationNumber(reservation.getIdentificationNumber());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setNumberOfAdults(reservation.getNumberOfAdults());
        dto.setNumberOfChildren(reservation.getNumberOfChildren());
        dto.setStatus(reservation.getStatus());
        dto.setPricePerNight(reservation.getPricePerNight());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setDiscountAmount(reservation.getDiscountAmount());
        dto.setFinalAmount(reservation.getFinalAmount());
        dto.setPaymentStatus(reservation.getPaymentStatus());
        dto.setPaymentMethod(reservation.getPaymentMethod());
        dto.setTransactionId(reservation.getTransactionId());
        dto.setSpecialRequests(reservation.getSpecialRequests());
        dto.setBreakfastIncluded(reservation.getBreakfastIncluded());
        dto.setParkingRequired(reservation.getParkingRequired());
        dto.setAirportPickup(reservation.getAirportPickup());
        dto.setCheckInTime(reservation.getCheckInTime());
        dto.setCheckOutTime(reservation.getCheckOutTime());
        dto.setBookedBy(reservation.getBookedBy());
        dto.setNotes(reservation.getNotes());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        dto.setNumberOfNights((int) nights);
        
        return dto;
    }

    private Reservation convertToEntity(ReservationDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setRoomId(dto.getRoomId());
        reservation.setRoomNumber(dto.getRoomNumber());
        reservation.setGuestName(dto.getGuestName());
        reservation.setGuestEmail(dto.getGuestEmail());
        reservation.setGuestPhone(dto.getGuestPhone());
        reservation.setGuestAddress(dto.getGuestAddress());
        reservation.setIdentificationType(dto.getIdentificationType());
        reservation.setIdentificationNumber(dto.getIdentificationNumber());
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setNumberOfAdults(dto.getNumberOfAdults());
        reservation.setNumberOfChildren(dto.getNumberOfChildren());
        reservation.setPricePerNight(dto.getPricePerNight());
        reservation.setSpecialRequests(dto.getSpecialRequests());
        reservation.setBreakfastIncluded(dto.getBreakfastIncluded());
        reservation.setParkingRequired(dto.getParkingRequired());
        reservation.setAirportPickup(dto.getAirportPickup());
        reservation.setBookedBy(dto.getBookedBy());
        reservation.setNotes(dto.getNotes());
        return reservation;
    }

    private void updateReservationFromDTO(Reservation reservation, ReservationDTO dto) {
        reservation.setGuestName(dto.getGuestName());
        reservation.setGuestPhone(dto.getGuestPhone());
        reservation.setGuestAddress(dto.getGuestAddress());
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setNumberOfAdults(dto.getNumberOfAdults());
        reservation.setNumberOfChildren(dto.getNumberOfChildren());
        reservation.setPricePerNight(dto.getPricePerNight());
        reservation.setSpecialRequests(dto.getSpecialRequests());
        reservation.setBreakfastIncluded(dto.getBreakfastIncluded());
        reservation.setParkingRequired(dto.getParkingRequired());
        reservation.setAirportPickup(dto.getAirportPickup());
        reservation.setNotes(dto.getNotes());
    }
}
