package com.app.rurban.controller;

import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.services.AuthService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/status")
    public String getStatus() {
        return "up and running";
    }

    @PostMapping("/register-user")
    public ResponseEntity<Object> registerUser(@RequestBody AuthRegisterDTO authRegisterDTO) throws JSONException {

        JSONObject json = new JSONObject();
        try {
            return new ResponseEntity<>(authService.registerUser(authRegisterDTO), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintErrorMessage(e);
            json.put("error",errorMessage);
            System.out.println("error" + e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(json.toString());
        } catch (Exception e) {
            System.out.println("error" + e);
            json.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(json.toString());
        }
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<Void> verifyEmail(@RequestParam String email, @RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();

        try {
            authService.verifyEmail(email, token);
            headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/#/app/emailverified"); // Replace with your desired URL
        } catch(Exception e) {
            if(e.getMessage().equalsIgnoreCase("Already Verified")) {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/"); // Replace with your desired URL
            } else if(e.getMessage().equalsIgnoreCase("Invalid Token")) {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/"); // Replace with your desired URL
            } else {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/"); // Replace with your desired URL
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/register-er")
    public ResponseEntity<Object> registerEr(@RequestBody AuthRegisterDTO authRegisterDTO) throws JSONException {
        JSONObject json = new JSONObject();
        try {

            return new ResponseEntity<>(authService.registerEr(authRegisterDTO), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println("error" + e);
            String errorMessage = extractConstraintErrorMessage(e);
            json.put("error",errorMessage);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(json.toString());
        } catch (Exception e) {
            System.out.println("error" + e);
            json.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(json.toString());
        }
    }

    private String extractConstraintErrorMessage(DataIntegrityViolationException e) {
        String errorMessage = "";
        String constraintName = e.getCause().getMessage();
        String text = fetchConstraintColumn(constraintName, "Detail: Key ").split("=")[0].replaceAll("[()]", "");
        if (constraintName.contains("constraint ")) {
            errorMessage = "The entered " + text.split("=")[0] + " is already registered.";
        } else {
            errorMessage += "Unknown constraint";
        }
        return errorMessage;
    }

    public String fetchConstraintColumn(String inputString, String targetWord) {
        // Find the index of the target word in the input string
        int index = inputString.indexOf(targetWord);

        if (index == -1) {
            // Target word not found in the string
            return "";
        }

        // Calculate the start index for extracting the substring
        int startIndex = index + targetWord.length();

        // Check if startIndex + 10 exceeds the length of the input string
        if (startIndex + 10 > inputString.length()) {
            // Return remaining characters from startIndex to end of string
            return inputString.substring(startIndex);
        } else {
            // Return next 10 characters after the target word
            return inputString.substring(startIndex, startIndex + 10);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getLogin(@RequestBody AuthLoginDTO authLoginDTO) {
        AuthResponseDTO authResponse = new AuthResponseDTO();
        try {

            return new ResponseEntity<>(authService.loginUser(authLoginDTO), HttpStatus.OK);
        }
        catch(SecurityException e) {
            return new ResponseEntity<>(authResponse, HttpStatus.FORBIDDEN);
        } catch (InvalidAttributeValueException e) {
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        } catch(AccountNotFoundException e) {
            return new ResponseEntity<>(authResponse, HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
