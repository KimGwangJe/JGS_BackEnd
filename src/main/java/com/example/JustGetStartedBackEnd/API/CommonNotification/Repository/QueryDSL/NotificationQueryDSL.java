package com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Request.NotificationDTO;

import java.util.List;

public interface NotificationQueryDSL {
    void deleteByMemberId(Long memberId);
    List<NotificationDTO> findByMemberId(Long memberId);
}
