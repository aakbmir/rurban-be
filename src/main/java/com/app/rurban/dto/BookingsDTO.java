package com.app.rurban.dto;

import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingsDTO {
    private String patientId;
    private String clinicId;
    private Date bookingDate;
    private String ETA;
    private String checkedInStatus;
    private String bookingStatus;
}
