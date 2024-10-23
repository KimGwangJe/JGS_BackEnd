package com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;

import java.util.List;

public interface MatchNotificationQueryDSL {
    MatchNotification findByMatchPostIdAndTeamName(Long matchPostId, String teamName);

    void deleteAllByMatchPostId(Long matchPostId);

    List<MatchNotificationDTO> findByTeamNameIn(List<String> teamNames);
}
