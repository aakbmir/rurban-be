package com.app.rurban.repository;

import com.app.rurban.model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("select ui from UserInfo ui where ui.email = :email OR CAST(ui.phone AS string) = :email")
    UserInfo findByEmailOrPhone(String email);

    @Query("select ui from UserInfo ui where ui.registerType = :type")
    List<UserInfo> fetchHospitals(String type);


}
