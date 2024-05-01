package com.app.rurban.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInDTO {
    private String patientId;
    private String clinicId;
    private String ETA;
    private String checkInStatus;
    private Date checkInDate;
    private String bookingStatus;
    private Date bookingDate;
    private Date cancellationDate;
    private String position;
}
