package com.nsbm.group03.eventManagementService.config;

import com.nsbm.group03.eventManagementService.entity.Event;
import com.nsbm.group03.eventManagementService.entity.EventType;
import com.nsbm.group03.eventManagementService.entity.Venue;
import com.nsbm.group03.eventManagementService.entity.EventStatus;
import com.nsbm.group03.eventManagementService.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EventRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new Event(null, "EVT-001", "Perera - Fernando Poruwa Ceremony", EventType.WEDDING, Venue.GRAND_BALLROOM, 
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(30), LocalTime.of(18, 0), LocalTime.of(23, 30), 
                250, "Kasun Perera", "kasun.perera@email.com", "+94-77-1234567", EventStatus.CONFIRMED, 
                true, "Traditional Sri Lankan cuisine buffet with kottu, lamprais, and seafood platters", "Sound system, traditional Kandyan dancers stage, poruwa setup, lighting", 
                "Traditional Sri Lankan floral decorations, oil lamps, Kandyan art displays", 2850000.0, null, 950000.0, 1900000.0, "Premium Sri Lankan wedding package", null, null));
                
            repository.save(new Event(null, "EVT-002", "Ceylon Chamber of Commerce Business Summit", EventType.CONFERENCE, Venue.CONFERENCE_ROOM_A, 
                LocalDate.now().plusDays(15), LocalDate.now().plusDays(16), LocalTime.of(8, 30), LocalTime.of(17, 30), 
                120, "Aruna Wickremesinghe", "aruna.w@chamber.lk", "+94-11-2345678", EventStatus.CONFIRMED, 
                true, "Sri Lankan breakfast (string hoppers, roti) and lunch (rice & curry) for 2 days", "Projectors, podium, wireless mics, video conferencing, high-speed WiFi, whiteboards", 
                "Professional staging, banner displays, registration desk", 1250000.0, null, 450000.0, 800000.0, "Corporate business conference", null, null));
                
            repository.save(new Event(null, "EVT-003", "Avurudu New Year Celebration", EventType.PARTY, Venue.GARDEN, 
                LocalDate.now().plusDays(45), LocalDate.now().plusDays(45), LocalTime.of(14, 0), LocalTime.of(20, 0), 
                180, "Sachini Kumarasinghe", "sachini.k@hotel.com", "+94-71-9876543", EventStatus.PLANNED, 
                true, "Traditional Avurudu kavum, kokis, athirasa, milk rice, and sweetmeats", "Traditional games setup (kana mutti, raban playing), sound system, tables & chairs", 
                "Traditional decorations, clay pots, coconut leaves, kolam designs", 850000.0, null, 0.0, 850000.0, "Sinhala & Tamil New Year celebration", null, null));
                
            repository.save(new Event(null, "EVT-004", "Colombo Tech Expo 2026", EventType.CONFERENCE, Venue.GRAND_BALLROOM, 
                LocalDate.now().plusDays(60), LocalDate.now().plusDays(62), LocalTime.of(9, 0), LocalTime.of(18, 0), 
                300, "Mahinda Jayasuriya", "mahinda.j@techexpo.lk", "+94-77-3456789", EventStatus.PLANNED, 
                true, "International cuisine with Sri Lankan options - 3 days meals", "Multiple projectors, screens, exhibition booths, WiFi, charging stations", 
                "Professional expo setup, branded materials, registration counters", 4500000.0, null, 1500000.0, 3000000.0, "3-day technology exhibition", null, null));
                
            repository.save(new Event(null, "EVT-005", "Silva Family Aluth Avurudda Celebration", EventType.PARTY, Venue.POOLSIDE, 
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(20), LocalTime.of(15, 0), LocalTime.of(21, 0), 
                85, "Chaminda Silva", "chaminda.silva@email.com", "+94-76-8765432", EventStatus.CONFIRMED, 
                true, "BBQ with Sri Lankan spices, kottu station, desserts", "Music system, pool lighting, seating arrangements", 
                "Poolside decorations, lanterns, tropical flowers", 485000.0, null, 100000.0, 385000.0, "Private family celebration", null, null));
            
            repository.save(new Event(null, "EVT-006", "Annual Esala Perahera Cultural Night", EventType.PARTY, Venue.GARDEN, 
                LocalDate.now().plusDays(90), LocalDate.now().plusDays(90), LocalTime.of(18, 30), LocalTime.of(23, 0), 
                200, "Nadeeka Jayawardena", "nadeeka.j@cultural.lk", "+94-81-2345678", EventStatus.PLANNED, 
                true, "Traditional Sri Lankan dinner buffet", "Stage for cultural performances, traditional music system, seating", 
                "Cultural decorations, traditional lamps, flag lines", 1150000.0, null, 0.0, 1150000.0, "Cultural showcase event", null, null));
                
            System.out.println("✅ Initialized 6 Sri Lankan themed events");
        }
    }
}
