package com.app.rurban.services;

import com.app.rurban.RurbanApplication;
import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.dto.ResponseDTO;
import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import com.app.rurban.model.UserInfo;
import com.app.rurban.repository.ClinicRepository;
import com.app.rurban.repository.PatientRepository;
import com.app.rurban.repository.UserInfoRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.management.InvalidAttributeValueException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JwtService jwtService;

//    public List<UserInfo> fetchClinics() {
//        return userInfoRepository.fetchClinics("Clinics");
//    }
//
//    public List<UserInfo> fetchPatients() {
//        return userInfoRepository.fetchClinics("Patient");
//    }

    public Patient registerUser(AuthRegisterDTO authRegisterDTO) throws Exception {

        boolean isRegistered = verifyEmailOrPhoneAlreadyRegistered(authRegisterDTO.getEmail(), authRegisterDTO.getContact());

        if (isRegistered) {
            throw new Exception("The entered email or contact is already registered!!");
        }
        Patient savedPatient = patientRepository.save(convertModelToPatientEntity(authRegisterDTO));
        UserInfo ui = saveLoginCredentials(savedPatient.getPatientEmail(), authRegisterDTO.getPassword());
        sendEmail(savedPatient.getPatientName(), ui.getEmail(), ui.getToken());
        return savedPatient;
    }

    private boolean verifyEmailOrPhoneAlreadyRegistered(String email, long contact) {
        return (patientRepository.findCountByEmailAndContact(email, contact) > 0)
                || (clinicRepository.findCountByEmailAndContact(email, contact) > 0);
    }

    public Clinic registerEr(AuthRegisterDTO authRegisterDTO) throws Exception {
        boolean isRegistered = verifyEmailOrPhoneAlreadyRegistered(authRegisterDTO.getEmail(), authRegisterDTO.getContact());

        if (isRegistered) {
            throw new Exception("The email or contact you entered is already registered!!");
        }

        Clinic savedClinic = clinicRepository.save(convertModelToErEntity(authRegisterDTO));
        UserInfo ui = saveLoginCredentials(savedClinic.getClinicEmail(), authRegisterDTO.getPassword());
        sendEmail(savedClinic.getClinicName(), ui.getEmail(), ui.getToken());
        return savedClinic;
    }

    public void sendEmail(String name, String toEmail, String token) {
        try {
            UserInfo ui = userInfoRepository.findByEmailOrPhone(toEmail);
            ui.setToken(token);
            userInfoRepository.save(ui);
            emailSenderService.sendMimeEmail(name, toEmail, "Confirm your Registration!", token);
        } catch (Exception e) {
            System.out.println("error while sending email");
        }
    }


    public UserInfo saveLoginCredentials(String email, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfo.setToken(String.valueOf(UUID.randomUUID()));
        return userInfoRepository.save(userInfo);
    }

    private Patient convertModelToPatientEntity(AuthRegisterDTO authRegisterDTO) {
        Patient auth = new Patient();
        auth.setPatientName(authRegisterDTO.getName());
        auth.setPatientDob(authRegisterDTO.getDob());
        //auth.setPatientEmail(authRegisterDTO.getName().replace(" ","").concat("@gmail.com"));
        auth.setPatientEmail(authRegisterDTO.getEmail());
        auth.setPatientContact(authRegisterDTO.getContact());
        auth.setPatientCurrentLocation(authRegisterDTO.getLocation());
        return auth;
    }

    private Clinic convertModelToErEntity(AuthRegisterDTO authRegisterDTO) {
        Clinic auth = new Clinic();
        auth.setClinicName(authRegisterDTO.getName());
        auth.setClinicEmail(authRegisterDTO.getEmail());
        auth.setClinicContact(authRegisterDTO.getContact());
        auth.setClinicLocation(authRegisterDTO.getLocation());
        auth.setClinicWebsite(authRegisterDTO.getWebsite());
        auth.setClinicAddress(authRegisterDTO.getAddress());
        return auth;
    }

    public ResponseDTO loginUser(AuthLoginDTO authLoginDTO) throws InvalidAttributeValueException, AccountNotFoundException, JSONException {
        UserInfo userInfo = userInfoRepository.findByEmailOrPhone(authLoginDTO.getUsername());
        if (userInfo != null) {
            if (userInfo.getVerified() == null) {
                throw new SecurityException("Email Not Verified");
            }
            if (userInfo.getPassword().equalsIgnoreCase(authLoginDTO.getPassword())) {
                return generateJwtToken(userInfo.getEmail());
            } else {
                throw new InvalidAttributeValueException("Invalid Credentials");
            }
        } else {
            throw new AccountNotFoundException("User does not exist");
        }
    }

    private ResponseDTO generateJwtToken(String email) throws JSONException {
        ResponseDTO authResponseDTO = new ResponseDTO();

        String registerType = "";
        Patient p = patientRepository.findByEmail(email);
        //JSONObject json = new JSONObject();
        if (p == null) {
            Clinic c = clinicRepository.findByEmail(email);
            if (c != null) {

                authResponseDTO.addDataField("token", jwtService.generateToken(email));
                authResponseDTO.addDataField("id", c.getId());
                authResponseDTO.addDataField("name", c.getClinicName());
                authResponseDTO.addDataField("registerType", "Hospital");
                //authResponseDTO.setData(json);
            }
        } else {
          //  json.put("details", new JSONObject().put("token",jwtService.generateToken(email)).put("id", p.getId()).put("name", p.getPatientName()).put("registerType", "Hospital"));
            //authResponseDTO.setData(json);

            authResponseDTO.addDataField("token", jwtService.generateToken(email));
            authResponseDTO.addDataField("id", p.getId());
            authResponseDTO.addDataField("name", p.getPatientName());
            authResponseDTO.addDataField("registerType", "Patient");
        }
        authResponseDTO.setSuccess(true);
        authResponseDTO.setMessage("Success");
        return authResponseDTO;
    }

    public String verifyEmail(String email, String token) throws Exception {
        UserInfo userInfo = userInfoRepository.findByEmailOrPhone(email);
        if (userInfo.getVerified() != null) {
            throw new Exception("Already Verified");
        } else {
            if (userInfo.getToken().equalsIgnoreCase(token)) {
                userInfo.setVerified("Verified");
                userInfoRepository.save(userInfo);
                return "success";
            } else {
                throw new Exception("Invalid Token");
            }
        }
    }
}
