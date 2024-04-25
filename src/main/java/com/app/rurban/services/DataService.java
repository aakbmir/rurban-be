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
import java.util.List;

@Service
public class DataService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    public List<Clinic> fetchClinics() {
        return (List<Clinic>) clinicRepository.findAll();
    }

//    public List<UserInfo> fetchPatients() {
//        return userInfoRepository.fetchClinics("Patient");
//    }


}
