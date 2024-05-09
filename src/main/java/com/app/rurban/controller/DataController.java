package com.app.rurban.controller;

import com.app.rurban.dto.CheckInDTO;
import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.services.CheckInService;
import com.app.rurban.services.DataService;
import org.json.JSONObject;
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
            if (e.getMessage().equalsIgnoreCase("Existing Checkin Pending")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
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

    @GetMapping("/fetch-user-checkins")
    public ResponseEntity<Object> fetchUserCheckins(@RequestParam String userId, @RequestParam String records) {
        try {
            List<CheckIns> clinics = checkInService.fetchUserCheckins(Long.valueOf(userId), records);
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

    @GetMapping("/fetch-hospital-checkins")
    public ResponseEntity<Object> fetchHospitalCheckins(@RequestParam String clinicId) {
        try {
            List<CheckIns> clinics = checkInService.fetchHospitalCheckins(Long.valueOf(clinicId));
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }


    @GetMapping("/fetch-past-hospital-checkins")
    public ResponseEntity<Object> fetchPastHospitalCheckins(@RequestParam String clinicId) {
        try {
            List<CheckIns> clinics = checkInService.fetchPastHospitalCheckins(Long.valueOf(clinicId));
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("/clinic-checkin-update")
    public ResponseEntity<Object> clinicCheckinUpdate(@RequestBody String input) {
        try {

            JSONObject json = new JSONObject(input);
            CheckIns clinics = checkInService.clinicCheckinUpdate(json.getString("id"), json.getString("action"));
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
