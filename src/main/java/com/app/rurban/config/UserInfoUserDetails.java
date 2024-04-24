package com.app.rurban.config;

public class UserInfoUserDetails {}
//implements UserDetails {
//    private String name;
//    private String password;
//    private List<GrantedAuthority> authorities;
//
//    public UserInfoUserDetails(UserInfo loginInfo) {
//        name = loginInfo.getEmail();
//        password = loginInfo.getPassword();
//    }
//
////    private List<GrantedAuthority> getAuthorities(Set<UserRoles> userRoles) {
////        return userRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())).collect(Collectors.toList());
////    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return name;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}