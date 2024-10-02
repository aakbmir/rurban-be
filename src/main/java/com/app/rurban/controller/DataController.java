package com.app.rurban.controller;

import com.app.rurban.dto.CheckInDTO;
import com.app.rurban.dto.ResponseDTO;
import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.services.CheckInService;
import com.app.rurban.services.DataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(DataController.class);

    @Autowired
    CheckInService checkInService;

    @Autowired
    DataService dataService;

    @GetMapping("/status")
    public ResponseEntity<Object> getStatus() {
        return new ResponseEntity<>(dataService.getCurrentLoc(), HttpStatus.OK);
    }

    @PostMapping("/create-checkin")
    public ResponseEntity<Object> createCheckIns(@RequestBody CheckInDTO checkInDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            CheckIns checkInsObj = checkInService.createCheckIns(checkInDTO);
            responseDTO.setData(checkInsObj);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            if (e.getMessage().equalsIgnoreCase("Existing Checkin Pending")) {
                return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/cancel-checkin")
    public ResponseEntity<Object> cancelCheckIns(@RequestParam String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            CheckIns checkInsObj = checkInService.cancelCheckIns(id);
            responseDTO.setData(checkInsObj);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-user-checkins")
    public ResponseEntity<Object> fetchUserCheckins(@RequestParam String userId, @RequestParam String records) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            List<CheckIns> clinics = checkInService.fetchUserCheckins(Long.valueOf(userId), records);
            responseDTO.setData(clinics);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-clinics")
    public ResponseEntity<Object> fetchClinics() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            List<Clinic> clinics = dataService.fetchClinics();
            responseDTO.setData(clinics);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/fetch-hospital-checkins")
    public ResponseEntity<Object> fetchHospitalCheckins(@RequestParam String clinicId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            List<CheckIns> clinics = checkInService.fetchHospitalCheckins(Long.valueOf(clinicId));
            responseDTO.setData(clinics);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }


    @GetMapping("/fetch-past-hospital-checkins")
    public ResponseEntity<Object> fetchPastHospitalCheckins(@RequestParam String clinicId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);
            List<CheckIns> clinics = checkInService.fetchPastHospitalCheckins(Long.valueOf(clinicId));
            responseDTO.setData(clinics);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/clinic-checkin-update")
    public ResponseEntity<Object> clinicCheckinUpdate(@RequestBody String input) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setSuccess(true);

            JSONObject json = new JSONObject(input);
            CheckIns clinics = checkInService.clinicCheckinUpdate(json.getString("id"), json.getString("action"));
            responseDTO.setData(clinics);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
