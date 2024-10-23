package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.DTO.JoinNotificationDTO;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;

import java.util.List;

public interface TeamJoinNotificationQueryDSL {
    JoinNotification findByMemberIdAndCommunityId(Long memberId, Long communityId);

    List<JoinNotificationDTO> findByWriterMemberId(Long memberId);

    void updateReadStatusByMemberId(Long memberId);
}
