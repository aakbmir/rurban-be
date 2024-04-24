package com.app.rurban.controller;

import com.app.rurban.model.UserInfo;
import com.app.rurban.services.AuthService;
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
    AuthService authService;

    @GetMapping("/status")
    public String getStatus() {
        return "up and running";
    }

    @GetMapping("/fetch-hospitals")
    public ResponseEntity<Object> fetchHospitals() {
        try {
            List<UserInfo> user = authService.fetchHospitals();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

}
