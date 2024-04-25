package com.app.rurban.repository;

import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends CrudRepository<Clinic, Long> {

    @Query("select count(*) from Clinic c where upper(c.clinicEmail) = upper(:email) OR c.clinicContact = :contact")
    int findCountByEmailAndContact(String email, long contact);

    @Query("select c from Clinic c where upper(c.clinicEmail) = upper(:email)")
    Clinic findByEmail(String email);
}
