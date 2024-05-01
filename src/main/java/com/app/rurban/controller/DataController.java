package com.app.rurban.controller;

import com.app.rurban.dto.CheckInDTO;
import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.services.CheckInService;
import com.app.rurban.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/data")
@CrossOrigin("*")
public class DataController {

    @Autowired
    CheckInService checkInService;

    @Autowired
    DataService dataService;

    @GetMapping("/status")
    public String getStatus() {
        return "up and running";
    }

    @PostMapping("/create-checkin")
    public ResponseEntity<Object> createCheckIns(@RequestBody CheckInDTO checkInDTO) {
        try {

            CheckIns checkInsObj = checkInService.createCheckIns(checkInDTO);
            return new ResponseEntity<>(checkInsObj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/cancel-checkin")
    public ResponseEntity<Object> cancelCheckIns(@RequestParam String id) {
        try {
            CheckIns checkInsObj = checkInService.cancelCheckIns(id);
            return new ResponseEntity<>(checkInsObj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-upcoming-appointments")
    public ResponseEntity<Object> fetchUpcomingAppointments(@RequestParam String userId) {
        try {
            List<CheckIns> clinics = checkInService.fetchUpcomingAppointments(Long.valueOf(userId));
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-clinics")
    public ResponseEntity<Object> fetchClinics() {
        try {
            List<Clinic> clinics = dataService.fetchClinics();
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-past-appointments")
    public ResponseEntity<Object> fetchPastAppointments(@RequestParam String clinicId) {
        try {
            List<CheckIns> clinics = checkInService.fetchPastAppointments(Long.valueOf(clinicId));
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

}
