package com.app.rurban.controller;

import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.dto.ResponseDTO;
import com.app.rurban.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @GetMapping("/status")
    public String getStatus() {
        return "up and running";
    }

    @PostMapping("/register-user")
    public ResponseEntity<Object> registerUser(@RequestBody AuthRegisterDTO authRegisterDTO) throws JSONException {

        logger.info("register-user : {}", authRegisterDTO.toString());


        ResponseDTO responseDTO = new ResponseDTO();
        try {
            logger.info("registerEr : {}", authRegisterDTO.toString());
            responseDTO.setSuccess(true);
            responseDTO.setError(null);
            responseDTO.setMessage("User successfully Registered");
            responseDTO.setData(authService.registerUser(authRegisterDTO));
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println("error" + e);
            String errorMessage = extractConstraintErrorMessage(e);
            responseDTO.setSuccess(false);
            responseDTO.setError("Failed to register");
            responseDTO.setMessage(errorMessage);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
        } catch (Exception e) {
            System.out.println("error" + e);
            responseDTO.setSuccess(false);
            responseDTO.setError("Failed to register");
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<Void> verifyEmail(@RequestParam String email, @RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();
        logger.info("verifyEmail : {}", email);
        try {
            authService.verifyEmail(email, token);
            headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/#/app/emailverified"); // Replace with your desired URL
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("Already Verified")) {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/#/app/verifiedAccount"); // Replace with your desired URL
            } else if (e.getMessage().equalsIgnoreCase("Invalid Token")) {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/"); // Replace with your desired URL
            } else {
                headers.add(HttpHeaders.LOCATION, "https://rurban-fe.onrender.com/#/app/verificationFailed"); // Replace with your desired URL
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/resendVerificationEmail")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam String email) {
        try {
            String token = String.valueOf(UUID.randomUUID());
            authService.sendEmail("user", email, token);
            return new ResponseEntity<>("resend token", HttpStatus.FOUND);
        } catch (Exception e) {
            System.out.println("error email");
            return new ResponseEntity<>("error email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/register-er")
    public ResponseEntity<Object> registerEr(@RequestBody AuthRegisterDTO authRegisterDTO) throws JSONException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            logger.info("registerEr : {}", authRegisterDTO.toString());
            responseDTO.setSuccess(true);
            responseDTO.setError(null);
            responseDTO.setMessage("User successfully Registered");
            responseDTO.setData(authService.registerEr(authRegisterDTO));
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println("error" + e);
            String errorMessage = extractConstraintErrorMessage(e);
            responseDTO.setSuccess(false);
            responseDTO.setError("Failed to register");
            responseDTO.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO.toString());
        } catch (Exception e) {
            System.out.println("error" + e);
            responseDTO.setSuccess(false);
            responseDTO.setError("Failed to register");
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO.toString());
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
        ResponseDTO responseDTO = new ResponseDTO();
        logger.info("getLogin : {}", authLoginDTO.toString());
        try {
            return new ResponseEntity<>(authService.loginUser(authLoginDTO), HttpStatus.OK);
        } catch (SecurityException e) {
            responseDTO = generateResponse(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
        } catch (InvalidAttributeValueException e) {
            responseDTO = generateResponse(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
        } catch (AccountNotFoundException e) {
            responseDTO = generateResponse(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseDTO = generateResponse(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseDTO generateResponse(String message) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(false);
        responseDTO.setError("Failed to login");
        responseDTO.setMessage(message);
        return responseDTO;
    }
}
