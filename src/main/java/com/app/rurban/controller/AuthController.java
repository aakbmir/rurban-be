package com.app.rurban.controller;

import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.model.UserInfo;
import com.app.rurban.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<Object> getRegister(@RequestBody AuthRegisterDTO authRegisterDTO) {
        try {
            System.out.println(authRegisterDTO);
            return new ResponseEntity<>(authService.registerUser(authRegisterDTO), HttpStatus.OK);

        }catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintErrorMessage(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        catch (Exception e) {
            return new ResponseEntity<>(authService.registerUser(authRegisterDTO), HttpStatus.OK);
        }
    }

    private String extractConstraintErrorMessage(DataIntegrityViolationException e) {
        String errorMessage = "";

        // Extract the specific constraint name from the exception message
        String constraintName = e.getCause().getMessage();
        String text = fetchConstraintColumn(constraintName, "Detail: Key ").split("=")[0].replaceAll("[()]", "");
        if (constraintName.contains("constraint ")) {
            errorMessage = "The entered "+ text.split("=")[0] + " is already registered.";
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
            System.out.println(authLoginDTO);
            return new ResponseEntity<>(authService.loginUser(authLoginDTO), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }

    }
}
