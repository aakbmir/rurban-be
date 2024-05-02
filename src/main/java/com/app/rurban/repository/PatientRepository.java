package com.app.rurban.repository;

import com.app.rurban.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

    @Query("select count(*) from Patient p where upper(p.patientEmail) = upper(:email) OR p.patientContact = :contact")
    int findCountByEmailAndContact(String email, long contact);

    @Query("select p from Patient p where upper(p.patientEmail) = upper(:email)")
    Patient findByEmail(String email);
}
