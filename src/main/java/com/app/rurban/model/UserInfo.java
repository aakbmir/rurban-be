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
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String dob;

    @Column(unique=true)
    private String email;

    @Column(unique=true)
    private long phone;

    private String password;

    private String registerType;

    private String position;

    private String lastLogin;
}
