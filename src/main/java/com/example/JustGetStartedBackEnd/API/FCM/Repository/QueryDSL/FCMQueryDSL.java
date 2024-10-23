package com.example.JustGetStartedBackEnd.API.FCM.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.FCM.Entity.FCMToken;

import java.util.List;
import java.util.Optional;

public interface FCMQueryDSL {
    Optional<FCMToken> findByMemberId(Long memberId);
    List<String> findAllFCMTokens();
}
