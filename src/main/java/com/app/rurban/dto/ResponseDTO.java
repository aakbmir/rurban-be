package com.app.rurban.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseDTO {
    private boolean success;
    private String error;
    private String message;
    private Object data;
    private DetailsDTO details = new DetailsDTO();
    private String token;
    private String registerType;
}
