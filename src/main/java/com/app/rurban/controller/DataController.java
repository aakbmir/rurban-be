package com.app.rurban.controller;

import com.app.rurban.dto.BookingsDTO;
import com.app.rurban.model.Bookings;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.UserInfo;
import com.app.rurban.services.AuthService;
import com.app.rurban.services.BookingsService;
import com.app.rurban.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/data")
@CrossOrigin("*")
public class DataController {

    @Autowired
    BookingsService bookingsService;

    @Autowired
    DataService dataService;

    @GetMapping("/status")
    public String getStatus() {
        return "up and running";
    }

    @PostMapping("/create-booking")
    public ResponseEntity<Object> createBooking(@RequestBody BookingsDTO bookings) {
        try {

            Bookings bookingsObj = bookingsService.createBooking(bookings);
            return new ResponseEntity<>(bookingsObj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }


    @GetMapping("/fetch-Bookings")
    public ResponseEntity<Object> fetchBookings(@RequestParam String userId) {
        try {
            List<Bookings> clinics = bookingsService.fetchBookings(Long.valueOf(userId));
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

    @GetMapping("/fetch-patients")
    public ResponseEntity<Object> fetchPatients() {
        try {
            List<UserInfo> user = new ArrayList<>();
            //List<UserInfo> user = authService.fetchPatients();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

}
