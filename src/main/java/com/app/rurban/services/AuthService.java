package com.app.rurban.services;

import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import com.app.rurban.model.UserInfo;
import com.app.rurban.repository.ClinicRepository;
import com.app.rurban.repository.PatientRepository;
import com.app.rurban.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InvalidAttributeValueException;
import javax.security.auth.login.AccountNotFoundException;

@Service
public class AuthService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

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
            throw new Exception("Email or Contact Already Registered!!");
        }
        Patient savedPatient = patientRepository.save(convertModelToPatientEntity(authRegisterDTO));
        saveLoginCredentials(savedPatient.getPatientEmail(), authRegisterDTO.getPassword());
        return savedPatient;
    }

    private boolean verifyEmailOrPhoneAlreadyRegistered(String email, long contact) {
        return (patientRepository.findCountByEmailAndContact(email, contact) > 0)
                || (clinicRepository.findCountByEmailAndContact(email, contact) > 0);
    }

    public Clinic registerEr(AuthRegisterDTO authRegisterDTO) throws Exception {
        boolean isRegistered = verifyEmailOrPhoneAlreadyRegistered(authRegisterDTO.getEmail(), authRegisterDTO.getContact());

        if (isRegistered) {
            throw new Exception("Email or Contact Already Registered!!");
        }
        Clinic savedClinic = clinicRepository.save(convertModelToErEntity(authRegisterDTO));
        saveLoginCredentials(savedClinic.getClinicEmail(), authRegisterDTO.getPassword());
        return savedClinic;
    }

    public void saveLoginCredentials(String email, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfoRepository.save(userInfo);
    }

    private Patient convertModelToPatientEntity(AuthRegisterDTO authRegisterDTO) {
        Patient auth = new Patient();
        auth.setPatientName(authRegisterDTO.getName());
        auth.setPatientDob(authRegisterDTO.getDob());
        auth.setPatientEmail(authRegisterDTO.getName().replace(" ","").concat("@gmail.com"));
//        auth.setPatientEmail(authRegisterDTO.getEmail());
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
        auth.setClinicWebsite(authRegisterDTO.getLocation());
        return auth;
    }

    public AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO) throws InvalidAttributeValueException, AccountNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmailOrPhone(authLoginDTO.getUsername());
        if (userInfo != null) {
            if (userInfo.getPassword().equalsIgnoreCase(authLoginDTO.getPassword())) {
                return loginDetails(userInfo.getEmail());
            } else {
                throw new InvalidAttributeValueException("Invalid Credentials");
            }
        } else {
            throw new AccountNotFoundException("User does not exist");
        }
    }

    private AuthResponseDTO loginDetails(String email) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();

        String registerType = "";
        Patient p = patientRepository.findByEmail(email);
        if (p == null) {
            Clinic c = clinicRepository.findByEmail(email);
            if (c != null) {
                authResponseDTO.getDetails().setId(c.getId());
                authResponseDTO.getDetails().setName(c.getClinicName());
                registerType = "Hospital";
            }
        } else {
            authResponseDTO.getDetails().setId(p.getId());
            authResponseDTO.getDetails().setName(p.getPatientName());
            registerType = "Patient";
        }

        authResponseDTO.setMessage("Success");
        authResponseDTO.setRegisterType(registerType);
        return authResponseDTO;
    }

    //    private AuthResponseDTO generateJwtToken(String username, UserInfo userInfo, AuthResponseDTO authResponse) {
//        String token = "";
//        try {
//            token = CommonUtils.lastToken.get(username);
//            //jwtService.extractExpiration(token);
//            authResponse.setRegisterType(userInfo.getRegisterType());
//            return loginDetails(token, userInfo, username, authResponse);
//        } catch (Exception e) {
//            //token = jwtService.generateToken(username);
//            return loginDetails(token, userInfo, username, authResponse);
//        }
//    }
}
