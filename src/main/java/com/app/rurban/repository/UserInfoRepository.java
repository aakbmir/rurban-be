package com.app.rurban.repository;

import com.app.rurban.model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("select ui from UserInfo ui where upper(ui.email) = upper(:email)")
    UserInfo findByEmailOrPhone(String email);

//    @Query("select ui from UserInfo ui where upper(ui.registerType) = upper(:type)")
//    List<UserInfo> fetchClinics(String type);


}
