package com.app.rurban.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRegisterDTO {
    private String name;
    private String dob;
    private String email;
    private long contact;
    private String location;
    private String website;
    private String registerType;
    private String password;
}
