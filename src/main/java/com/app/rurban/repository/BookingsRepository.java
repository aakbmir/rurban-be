package com.app.rurban.repository;

import com.app.rurban.model.Bookings;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends CrudRepository<Bookings, Long> {
    @Query("select b from Bookings b where b.patientId = :userId and b.bookingStatus = 'Confirmed' order by bookingDate desc")
    List<Bookings> findByPatientIdAndConfirmedStatus(Patient userId);

    @Query("select b from Bookings b where b.clinicId = :clinicId order by bookingDate desc")
    List<Bookings> findByClinicId(Clinic clinicId);

    @Query("select b from Bookings b where b.patientId = :userId order by bookingDate desc")
    List<Bookings> findByPatientId(Patient userId);
}
