package com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification;

import java.util.List;

public interface NotificationQueryDSL {
    void deleteByMemberId(Long memberId);
    List<Notification> findByMemberId(Long memberId);
}
