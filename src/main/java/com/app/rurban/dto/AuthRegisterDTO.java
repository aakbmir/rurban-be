package com.app.rurban.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthRegisterDTO {
    private String name;
    private String dob;
    private String email;
    private long contact;
    private String location;
    private String address;
    private String website;
    private String registerType;
    private String password;
}
