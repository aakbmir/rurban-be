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
public class CheckIns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinicId;

    // scheduled, checked-in, completed, no-show,
    private String checkInStatus;

    private Date checkInDate;

    private String ETA;

    //booked, cancelled
    private String bookingStatus;

    private Date bookingDate;

    private Date bookingCancellationDate;

    private String patientLocation;
}
