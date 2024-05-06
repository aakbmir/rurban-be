package com.app.rurban.services;

import com.app.rurban.dto.CheckInDTO;
import com.app.rurban.model.CheckIns;
import com.app.rurban.model.Clinic;
import com.app.rurban.model.Patient;
import com.app.rurban.repository.CheckInsRepository;
import com.app.rurban.repository.ClinicRepository;
import com.app.rurban.repository.PatientRepository;
import com.app.rurban.utils.CommonConstants;
import com.app.rurban.utils.CommonUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CheckInService {

    @Autowired
    CheckInsRepository checkInsRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    RestTemplate restTemplate;

    public CheckIns createCheckIns(CheckInDTO checkInDTO) {
        CheckIns checkIns = mapToCheckIns(checkInDTO);


        return checkInsRepository.save(checkIns);
    }

    private String calculateETA(Clinic clinic, CheckInDTO checkInDTO) {
        try {


            String api = "https://api.openrouteservice.org/v2/directions/driving-car?";
            String api_key = "api_key=" + CommonUtils.API_KEY;
            String origin = "&start=" + checkInDTO.getPosition().split(",")[1] + "," + checkInDTO.getPosition().split(",")[0];
            String destination = "&end=" + clinic.getClinicLocation().split(",")[1] + "," + clinic.getClinicLocation().split(",")[0];
            String url = api + api_key + origin + destination;
            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            String res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            try {
                JSONObject geoResponse1 = new JSONObject(res);
                JSONArray durations = geoResponse1.getJSONArray("durations");
                Double duration = durations.getJSONArray(0).getDouble(0);
            } catch (Exception e) {
                System.out.println();
            }

            JSONObject geoResponse = new JSONObject(res);
            JSONArray features = geoResponse.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            JSONObject summary = properties.getJSONObject("summary");
            int duration = Integer.valueOf((int) summary.getDouble("duration") / 60);

            return String.valueOf(duration);

        } catch (Exception e) {
            return "NA";
        }
    }

    private CheckIns mapToCheckIns(CheckInDTO checkInDTO) {
        Patient patient = patientRepository.findById(Long.valueOf(checkInDTO.getPatientId()))
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Clinic clinic = clinicRepository.findById(Long.valueOf(checkInDTO.getClinicId()))
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        CheckIns checkIns = new CheckIns();
        checkIns.setBookingDate(new Date());
        checkIns.setBookingStatus(CommonConstants.BOOKED);
        checkIns.setClinicId(clinic);
        checkIns.setPatientId(patient);
        checkIns.setPatientLocation(checkInDTO.getPosition());
        checkIns.setETA(calculateETA(clinic, checkInDTO));
        Date currDate = new Date();
        int minutesToAdd = Integer.valueOf(checkIns.getETA());
        Date etaTime = DateUtils.addMinutes(currDate, minutesToAdd);
        checkIns.setEstimatedAppointmentTime(etaTime);
        return checkInsRepository.save(checkIns);

    }

    public List<CheckIns> fetchUserCheckins(Long userId, String records) {
        Patient patient = patientRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        if (records.equalsIgnoreCase("all")) {
            return checkInsRepository.findByIdAndAllCheckins(patient);
        } else {
            return checkInsRepository.findByIdAndUpcomingCheckins(patient);
        }
    }

    public List<CheckIns> fetchHospitalCheckins(Long clinicId) {
        Clinic clinic = clinicRepository.findById(Long.valueOf(clinicId))
                .orElseThrow(() -> new RuntimeException("checkIn not found"));
        return checkInsRepository.findUpcomingCheckinsByClinicId(clinic);
    }

    public List<CheckIns> fetchPastHospitalCheckins(Long clinicId) {
        Clinic clinic = clinicRepository.findById(Long.valueOf(clinicId))
                .orElseThrow(() -> new RuntimeException("checkIn not found"));
        return checkInsRepository.findPastCheckinsByClinicId(clinic);
    }
    public CheckIns cancelCheckIns(String id) {
        CheckIns checkIn = checkInsRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("checkIn not found"));
        checkIn.setBookingStatus("Cancelled");
        checkIn.setBookingCancellationDate(new Date());
        return checkInsRepository.save(checkIn);
    }

    public CheckIns clinicCheckinUpdate(String id, String action) {
        CheckIns checkIn = checkInsRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("checkIn not found"));
        if (action.equalsIgnoreCase("checkIn")) {
            checkIn.setCheckInDate(new Date());
            checkIn.setCheckInStatus("Checked In");
        } else {
            checkIn.setCheckInCancellationDate(new Date());
            checkIn.setCheckInStatus("No Show");
        }

        return checkInsRepository.save(checkIn);
    }
}
