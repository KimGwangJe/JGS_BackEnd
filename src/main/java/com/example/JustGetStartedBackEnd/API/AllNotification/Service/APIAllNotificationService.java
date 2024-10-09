package com.example.JustGetStartedBackEnd.API.AllNotification.Service;

import com.example.JustGetStartedBackEnd.API.AllNotification.DTO.AllNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Service.APIMatchNotificationService;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Service.APITeamInviteService;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Service.APITeamJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class APIAllNotificationService {
    private final APIMatchNotificationService apiMatchNotificationService;
    private final APINotificationService apiNotificationService;
    private final APITeamInviteService apiTeamInviteService;
    private final APITeamJoinService apiTeamJoinService;

    @Transactional(readOnly = true)
    public AllNotificationDTO getAllNotificationDTO(Long memberId){
        AllNotificationDTO allNotificationDTO = new AllNotificationDTO();
        allNotificationDTO.setMatchNotificationListDTO(apiMatchNotificationService.getAllMatchNotifications(memberId));
        allNotificationDTO.setNotificationListDTO(apiNotificationService.getAllNotification(memberId));
        allNotificationDTO.setTeamInviteListDTO(apiTeamInviteService.getTeamInvite(memberId));
        allNotificationDTO.setJoinNotificationListDTO(apiTeamJoinService.getTeamJoinList(memberId));

        return allNotificationDTO;
    }
}
