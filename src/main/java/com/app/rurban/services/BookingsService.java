package com.app.rurban.services;

import com.app.rurban.dto.BookingsDTO;
import com.app.rurban.model.Bookings;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import com.app.rurban.repository.BookingsRepository;
import com.app.rurban.repository.ClinicRepository;
import com.app.rurban.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingsService {

    @Autowired
    BookingsRepository bookingsRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    public Bookings createBooking(BookingsDTO bookingsDTO) {
        Bookings bookings = mapToBookings(bookingsDTO);
        return bookingsRepository.save(bookings);
    }

    private Bookings mapToBookings(BookingsDTO bookingsDTO) {
        Patient patient = patientRepository.findById(Long.valueOf(bookingsDTO.getPatientId()))
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Clinic clinic = clinicRepository.findById(Long.valueOf(bookingsDTO.getClinicId()))
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        Bookings bookings = new Bookings();
        bookings.setBookingDate(bookingsDTO.getBookingDate());
                bookings.setBookingStatus(bookingsDTO.getBookingStatus());
                bookings.setCheckedInStatus(bookingsDTO.getCheckedInStatus());
                bookings.setClinicId(clinic);
                bookings.setPatientId(patient);
                bookings.setETA(bookingsDTO.getETA());
                bookings.setBookingDate(new Date());
                return bookingsRepository.save(bookings);

    }


    public List<Bookings> fetchBookings(Long userId) {
        Patient patient = patientRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return bookingsRepository.findByPatientId(patient);

    }
}
