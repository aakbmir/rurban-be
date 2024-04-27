package com.app.rurban.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinicId;


    private Date bookingDate;

    private String ETA;

    // scheduled, checked-in, in-progress, completed, no-show,
    private String checkedInStatus;

    //confirmed, cancelled
    private String bookingStatus;
}
