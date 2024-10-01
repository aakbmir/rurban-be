package com.app.rurban.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthLoginDTO {

        private String username;
        private String password;
        private String registerType;
}
