package com.app.rurban.repository;

import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInsRepository extends CrudRepository<CheckIns, Long> {
    @Query("select b from CheckIns b")
    //@Query("select b from CheckIns b where b.patientId = :userId and b.appointmentStatus = 'Confirmed' order by checkInDate desc")
    List<CheckIns> findByPatientIdAndConfirmedStatus(Patient userId);

    @Query("select b from CheckIns b where b.clinicId = :clinicId and b.checkInStatus is null order by bookingDate desc")
    List<CheckIns> findUpcomingCheckinsByClinicId(Clinic clinicId);


    @Query("select b from CheckIns b where b.clinicId = :clinicId and b.checkInStatus is not null order by bookingDate desc")
    List<CheckIns> findPastCheckinsByClinicId(Clinic clinicId);


    @Query("select b from CheckIns b where b.patientId = :userId and b.checkInStatus is null and b.bookingStatus = 'Booked' order by bookingDate desc")
    List<CheckIns> findByIdAndUpcomingCheckins(Patient userId);

    @Query("select b from CheckIns b where b.patientId = :userId order by bookingDate desc")
    List<CheckIns> findByIdAndAllCheckins(Patient userId);

    @Query("select b from CheckIns b where b.clinicId = :clinicId and b.patientId = :patientId and b.checkInStatus is null and b.bookingStatus = 'Booked' order by bookingDate desc")
    List<CheckIns> fetchOpenCheckIns(Clinic clinicId, Patient patientId);
}
