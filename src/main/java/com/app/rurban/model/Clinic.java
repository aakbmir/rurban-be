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
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String clinicName;

    @Column(unique = true)
    private String clinicEmail;

    @Column(unique = true)
    private long clinicContact;

    private String clinicLocation;

    private String clinicWebsite;
}
