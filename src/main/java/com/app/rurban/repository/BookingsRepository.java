package com.app.rurban.repository;

import com.app.rurban.model.Bookings;
import com.app.rurban.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends CrudRepository<Bookings, Long> {
    List<Bookings> findByPatientId(Patient userId);
}
