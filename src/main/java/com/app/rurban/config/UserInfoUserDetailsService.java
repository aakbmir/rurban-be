package com.app.rurban.config;

//@Service
public class UserInfoUserDetailsService{}
//implements UserDetailsService {
//
//    @Autowired
//    UserInfoRepository repository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserInfo loginInfo = CommonUtils.userInfoCache.get(username);
//        if (loginInfo == null) {
//            loginInfo = repository.findByEmail(username);
//            if (loginInfo == null) {
//                throw new UsernameNotFoundException("user not found " + username);
//            }
//            CommonUtils.userInfoCache.put(username, loginInfo);
//        }
//        return new UserInfoUserDetails(loginInfo);
//    }
//}