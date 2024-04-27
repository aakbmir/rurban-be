package com.app.rurban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private DetailsDTO details = new DetailsDTO();
    private String message;
    private String token;
    private String registerType;
}
