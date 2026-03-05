package com.nsbm.group03.reservationManagementService.config;

import com.nsbm.group03.reservationManagementService.entity.Reservation;
import com.nsbm.group03.reservationManagementService.entity.ReservationStatus;
import com.nsbm.group03.reservationManagementService.entity.PaymentStatus;
import com.nsbm.group03.reservationManagementService.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (reservationRepository.count() == 0) {
            initializeReservations();
        }
    }

    private void initializeReservations() {
        // Reservation 1 - Confirmed for future
        Reservation r1 = new Reservation();
        r1.setRoomId(1L);
        r1.setRoomNumber("101");
        r1.setGuestName("Nuwan Amarasinghe");
        r1.setGuestEmail("nuwan.a@email.com");
        r1.setGuestPhone("+94-77-1234567");
        r1.setGuestAddress("45 Galle Road, Colombo 03");
        r1.setIdentificationType("PASSPORT");
        r1.setIdentificationNumber("N1234567");
        r1.setCheckInDate(LocalDate.now().plusDays(2));
        r1.setCheckOutDate(LocalDate.now().plusDays(5));
        r1.setNumberOfGuests(2);
        r1.setNumberOfAdults(2);
        r1.setNumberOfChildren(0);
        r1.setStatus(ReservationStatus.CONFIRMED);
        r1.setPricePerNight(12500.0);
        r1.setTotalAmount(37500.0);
        r1.setDiscountAmount(0.0);
        r1.setFinalAmount(37500.0);
        r1.setPaymentStatus(PaymentStatus.PARTIAL_PAID);
        r1.setPaymentMethod("CREDIT_CARD");
        r1.setBreakfastIncluded(true);
        r1.setParkingRequired(true);
        r1.setAirportPickup(false);
        r1.setSpecialRequests("Late check-in after 9 PM");
        r1.setBookedBy("admin");

        // Reservation 2 - Current guest (checked in)
        Reservation r2 = new Reservation();
        r2.setRoomId(2L);
        r2.setRoomNumber("102");
        r2.setGuestName("Sanduni Fernando");
        r2.setGuestEmail("sanduni.fernando@email.com");
        r2.setGuestPhone("+94-71-2345678");
        r2.setGuestAddress("123 Duplication Road, Colombo 04");
        r2.setIdentificationType("NATIONAL_ID");
        r2.setIdentificationNumber("987654321V");
        r2.setCheckInDate(LocalDate.now().minusDays(1));
        r2.setCheckOutDate(LocalDate.now().plusDays(2));
        r2.setNumberOfGuests(3);
        r2.setNumberOfAdults(2);
        r2.setNumberOfChildren(1);
        r2.setStatus(ReservationStatus.CHECKED_IN);
        r2.setPricePerNight(12500.0);
        r2.setTotalAmount(37500.0);
        r2.setDiscountAmount(2500.0);
        r2.setFinalAmount(35000.0);
        r2.setPaymentStatus(PaymentStatus.PAID);
        r2.setPaymentMethod("DEBIT_CARD");
        r2.setTransactionId("TXN-LKR-123456");
        r2.setBreakfastIncluded(true);
        r2.setParkingRequired(false);
        r2.setAirportPickup(true);
        r2.setSpecialRequests("Extra towels and baby cot");
        r2.setBookedBy("admin");

        // Reservation 3 - Pending confirmation
        Reservation r3 = new Reservation();
        r3.setRoomId(3L);
        r3.setRoomNumber("201");
        r3.setGuestName("Priyantha Dissanayake");
        r3.setGuestEmail("priyantha.d@email.com");
        r3.setGuestPhone("+94-76-3456789");
        r3.setGuestAddress("89 Kandy Road, Kadawatha");
        r3.setIdentificationType("NATIONAL_ID");
        r3.setIdentificationNumber("876543210V");
        r3.setCheckInDate(LocalDate.now().plusDays(7));
        r3.setCheckOutDate(LocalDate.now().plusDays(10));
        r3.setNumberOfGuests(1);
        r3.setNumberOfAdults(1);
        r3.setNumberOfChildren(0);
        r3.setStatus(ReservationStatus.PENDING);
        r3.setPricePerNight(22000.0);
        r3.setTotalAmount(600.0);
        r3.setDiscountAmount(0.0);
        r3.setFinalAmount(600.0);
        r3.setPaymentStatus(PaymentStatus.PENDING);
        r3.setBreakfastIncluded(false);
        r3.setParkingRequired(true);
        r3.setAirportPickup(false);
        r3.setSpecialRequests("Quiet room please");
        r3.setBookedBy("admin");

        // Reservation 4 - Checked out
        Reservation r4 = new Reservation();
        r4.setRoomId(4L);
        r4.setRoomNumber("202");
        r4.setGuestName("Emily Brown");
        r4.setGuestEmail("emily.brown@email.com");
        r4.setGuestPhone("+1-555-0104");
        r4.setGuestAddress("321 Elm St, Miami, FL");
        r4.setIdentificationType("PASSPORT");
        r4.setIdentificationNumber("P7777777");
        r4.setCheckInDate(LocalDate.now().minusDays(5));
        r4.setCheckOutDate(LocalDate.now().minusDays(2));
        r4.setNumberOfGuests(2);
        r4.setNumberOfAdults(2);
        r4.setNumberOfChildren(0);
        r4.setStatus(ReservationStatus.CHECKED_OUT);
        r4.setPricePerNight(170.0);
        r4.setTotalAmount(510.0);
        r4.setDiscountAmount(10.0);
        r4.setFinalAmount(500.0);
        r4.setPaymentStatus(PaymentStatus.PAID);
        r4.setPaymentMethod("CASH");
        r4.setBreakfastIncluded(true);
        r4.setParkingRequired(true);
        r4.setAirportPickup(false);
        r4.setBookedBy("admin");

        // Reservation 5 - Cancelled
        Reservation r5 = new Reservation();
        r5.setRoomId(5L);
        r5.setRoomNumber("301");
        r5.setGuestName("David Wilson");
        r5.setGuestEmail("david.w@email.com");
        r5.setGuestPhone("+1-555-0105");
        r5.setGuestAddress("555 Cedar Ln, Boston, MA");
        r5.setIdentificationType("PASSPORT");
        r5.setIdentificationNumber("P8888888");
        r5.setCheckInDate(LocalDate.now().plusDays(1));
        r5.setCheckOutDate(LocalDate.now().plusDays(3));
        r5.setNumberOfGuests(4);
        r5.setNumberOfAdults(2);
        r5.setNumberOfChildren(2);
        r5.setStatus(ReservationStatus.CANCELLED);
        r5.setPricePerNight(250.0);
        r5.setTotalAmount(500.0);
        r5.setDiscountAmount(0.0);
        r5.setFinalAmount(500.0);
        r5.setPaymentStatus(PaymentStatus.REFUNDED);
        r5.setBreakfastIncluded(true);
        r5.setParkingRequired(true);
        r5.setAirportPickup(true);
        r5.setCancellationReason("Travel plans changed");
        r5.setBookedBy("admin");

        // Reservation 6 - Future booking with breakfast
        Reservation r6 = new Reservation();
        r6.setRoomId(6L);
        r6.setRoomNumber("302");
        r6.setGuestName("Sarah Davis");
        r6.setGuestEmail("sarah.davis@email.com");
        r6.setGuestPhone("+1-555-0106");
        r6.setGuestAddress("999 Maple Dr, Seattle, WA");
        r6.setIdentificationType("DRIVING_LICENSE");
        r6.setIdentificationNumber("D1111111");
        r6.setCheckInDate(LocalDate.now().plusDays(14));
        r6.setCheckOutDate(LocalDate.now().plusDays(21));
        r6.setNumberOfGuests(2);
        r6.setNumberOfAdults(2);
        r6.setNumberOfChildren(0);
        r6.setStatus(ReservationStatus.CONFIRMED);
        r6.setPricePerNight(220.0);
        r6.setTotalAmount(1540.0);
        r6.setDiscountAmount(140.0);
        r6.setFinalAmount(1400.0);
        r6.setPaymentStatus(PaymentStatus.PAID);
        r6.setPaymentMethod("ONLINE");
        r6.setTransactionId("TXN789012");
        r6.setBreakfastIncluded(true);
        r6.setParkingRequired(true);
        r6.setAirportPickup(true);
        r6.setSpecialRequests("Honeymoon package");
        r6.setBookedBy("admin");

        // Reservation 7 - Today's check-in
        Reservation r7 = new Reservation();
        r7.setRoomId(7L);
        r7.setRoomNumber("401");
        r7.setGuestName("Robert Miller");
        r7.setGuestEmail("robert.m@email.com");
        r7.setGuestPhone("+1-555-0107");
        r7.setGuestAddress("777 Birch St, Denver, CO");
        r7.setIdentificationType("PASSPORT");
        r7.setIdentificationNumber("P2222222");
        r7.setCheckInDate(LocalDate.now());
        r7.setCheckOutDate(LocalDate.now().plusDays(3));
        r7.setNumberOfGuests(1);
        r7.setNumberOfAdults(1);
        r7.setNumberOfChildren(0);
        r7.setStatus(ReservationStatus.CONFIRMED);
        r7.setPricePerNight(190.0);
        r7.setTotalAmount(570.0);
        r7.setDiscountAmount(0.0);
        r7.setFinalAmount(570.0);
        r7.setPaymentStatus(PaymentStatus.PENDING);
        r7.setBreakfastIncluded(false);
        r7.setParkingRequired(false);
        r7.setAirportPickup(false);
        r7.setSpecialRequests("Early check-in if possible");
        r7.setBookedBy("admin");

        // Reservation 8 - Today's check-out
        Reservation r8 = new Reservation();
        r8.setRoomId(8L);
        r8.setRoomNumber("402");
        r8.setGuestName("Lisa Anderson");
        r8.setGuestEmail("lisa.a@email.com");
        r8.setGuestPhone("+1-555-0108");
        r8.setGuestAddress("444 Spruce Ave, Portland, OR");
        r8.setIdentificationType("NATIONAL_ID");
        r8.setIdentificationNumber("N3333333");
        r8.setCheckInDate(LocalDate.now().minusDays(4));
        r8.setCheckOutDate(LocalDate.now());
        r8.setNumberOfGuests(3);
        r8.setNumberOfAdults(2);
        r8.setNumberOfChildren(1);
        r8.setStatus(ReservationStatus.CHECKED_IN);
        r8.setPricePerNight(210.0);
        r8.setTotalAmount(840.0);
        r8.setDiscountAmount(40.0);
        r8.setFinalAmount(800.0);
        r8.setPaymentStatus(PaymentStatus.PAID);
        r8.setPaymentMethod("CREDIT_CARD");
        r8.setTransactionId("TXN345678");
        r8.setBreakfastIncluded(true);
        r8.setParkingRequired(true);
        r8.setAirportPickup(false);
        r8.setSpecialRequests("Late checkout requested");
        r8.setBookedBy("admin");

        reservationRepository.save(r1);
        reservationRepository.save(r2);
        reservationRepository.save(r3);
        reservationRepository.save(r4);
        reservationRepository.save(r5);
        reservationRepository.save(r6);
        reservationRepository.save(r7);
        reservationRepository.save(r8);

        System.out.println("✅ Initialized 8 sample reservations (various statuses and dates)");
    }
}
