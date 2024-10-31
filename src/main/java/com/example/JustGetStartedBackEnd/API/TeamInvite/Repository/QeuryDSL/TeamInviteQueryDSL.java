package com.example.JustGetStartedBackEnd.API.TeamInvite.Repository.QeuryDSL;

import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;

import java.util.List;

public interface TeamInviteQueryDSL {
    List<TeamInviteInfoDTO> findByMemberId(Long memberId);

    void updateReadStatusByMemberId(Long memberId);

    TeamInviteNotification findByMemberIdAndTeamName(Long memberId, String teamName);
}
