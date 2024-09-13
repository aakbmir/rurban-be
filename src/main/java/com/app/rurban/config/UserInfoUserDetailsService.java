package com.app.rurban.config;

import com.app.rurban.model.UserInfo;
import com.app.rurban.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo loginInfo = userInfoRepository.findByEmailOrPhone(username);
        if (loginInfo == null) {
            throw new UsernameNotFoundException("user not found : " + username);
        }
        return new UserInfoUserDetails(loginInfo);
    }
}
