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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
            String origin = "&start=" + checkInDTO.getPosition().split(",")[0] + "," + checkInDTO.getPosition().split(",")[1];
            String destination = "&end=" + clinic.getClinicLocation().split(",")[0] + "," + clinic.getClinicLocation().split(",")[1];
            String url = api + api_key + origin + destination;
            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            String res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            System.out.println(res);
            try {
                JSONObject geoResponse1 = new JSONObject(res);
                JSONArray durations = geoResponse1.getJSONArray("durations");
                Double duration = durations.getJSONArray(0).getDouble(0);
            }catch (Exception e) {
                System.out.println();
            }
            try {
                JSONObject geoResponse = new JSONObject(res);
                JSONArray features = geoResponse.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject properties = feature.getJSONObject("properties");
                JSONObject summary = properties.getJSONObject("summary");
                Double duration = summary.getDouble("duration");
                System.out.println(duration);
            } catch(Exception e) {
                System.out.println(e);
            }
            return "20 Mins";
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
        checkIns.setCheckInStatus(CommonConstants.CONFIRMED);
        checkIns.setClinicId(clinic);
        checkIns.setPatientId(patient);
        checkIns.setPatientLocation(checkInDTO.getPosition());
        checkIns.setETA(calculateETA(clinic, checkInDTO));
        return checkInsRepository.save(checkIns);

    }

    public List<CheckIns> fetchUpcomingAppointments(Long userId) {
        Patient patient = patientRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return checkInsRepository.findByIdAndUpcomingCheckins(patient);
    }


    public List<CheckIns> fetchPastAppointments(Long clinicId) {
        Clinic clinic = clinicRepository.findByIdAndPastData(Long.valueOf(clinicId));
        return checkInsRepository.findByClinicId(clinic);

    }

    public CheckIns cancelCheckIns(String id) {
        CheckIns checkIn = checkInsRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("checkIn not found"));
        checkIn.setBookingStatus("Cancelled");
        checkIn.setBookingCancellationDate(new Date());
        return checkInsRepository.save(checkIn);
    }
}
