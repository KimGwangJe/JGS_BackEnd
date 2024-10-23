package com.example.JustGetStartedBackEnd.API.FCM.Repository;

import com.example.JustGetStartedBackEnd.API.FCM.Entity.FCMToken;
import com.example.JustGetStartedBackEnd.API.FCM.Repository.QueryDSL.FCMQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMRepository extends JpaRepository<FCMToken, Long>, FCMQueryDSL {
}
