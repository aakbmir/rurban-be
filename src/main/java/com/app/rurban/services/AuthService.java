package com.app.rurban.services;

import com.app.rurban.dto.AuthLoginDTO;
import com.app.rurban.dto.AuthRegisterDTO;
import com.app.rurban.dto.AuthResponseDTO;
import com.app.rurban.model.UserInfo;
import com.app.rurban.repository.UserInfoRepository;
import com.app.rurban.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    UserInfoRepository userInfoRepository;

    public List<UserInfo> fetchHospitals() {
        return userInfoRepository.fetchHospitals("Hospital");

    }

    public UserInfo registerUser(AuthRegisterDTO authRegisterDTO) {
        UserInfo auth = convertToModel(authRegisterDTO);
        return userInfoRepository.save(auth);
    }

    private UserInfo convertToModel(AuthRegisterDTO authRegisterDTO) {
        UserInfo auth = new UserInfo();

        auth.setName(authRegisterDTO.getName());
        auth.setDob(authRegisterDTO.getDob());
        auth.setEmail(authRegisterDTO.getEmail());
        auth.setPhone(authRegisterDTO.getPhone());
        auth.setPassword(authRegisterDTO.getPassword());
        auth.setRegisterType(authRegisterDTO.getRegisterType());
        auth.setPhone(authRegisterDTO.getPhone());
        auth.setPosition(authRegisterDTO.getPosition());
        return auth;
    }

    public AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO) throws Exception {
        AuthResponseDTO authResponse = new AuthResponseDTO();
        UserInfo userInfo = userInfoRepository.findByEmailOrPhone(authLoginDTO.getUsername());
        System.out.println(userInfo);
        if (userInfo != null && userInfo.getPassword().equalsIgnoreCase(authLoginDTO.getPassword())) {
                return generateJwtToken(authLoginDTO.getUsername(), userInfo, authResponse);
        } else {
            authResponse.setMessage("Invalid Credentials");
            throw new Exception(authResponse.getMessage());
        }
    }

    private AuthResponseDTO generateJwtToken(String username, UserInfo userInfo, AuthResponseDTO authResponse) {
        String token = "";
        try {
            token = CommonUtils.lastToken.get(username);
            //jwtService.extractExpiration(token);
            authResponse.setRegisterType(userInfo.getRegisterType());
            return loginDetails(token, userInfo, username, authResponse);
        } catch (Exception e) {
            //token = jwtService.generateToken(username);
            return loginDetails(token, userInfo, username, authResponse);
        }
    }

    private AuthResponseDTO loginDetails(String token, UserInfo userInfo, String username, AuthResponseDTO authResponseDTO) {
        authResponseDTO.setUsername(userInfo.getName());
        authResponseDTO.setToken(token);
        authResponseDTO.setMessage("Success");
        return authResponseDTO;
    }
}
