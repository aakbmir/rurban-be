package com.app.rurban.dto;

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
        private String password;
        private String registerType;
        private long phone;
}
