package com.app.rurban.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String patientName;

    private String patientDob;

    @Column(unique=true)
    private String patientEmail;

    @Column(unique=true)
    private long patientContact;

    private String patientCurrentLocation;


}
