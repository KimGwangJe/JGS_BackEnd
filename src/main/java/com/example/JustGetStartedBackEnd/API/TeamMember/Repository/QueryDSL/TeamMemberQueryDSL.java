package com.example.JustGetStartedBackEnd.API.TeamMember.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;

import java.util.List;

public interface TeamMemberQueryDSL {
    List<TeamMember> findAllByMemberId(Long memberId);
}
