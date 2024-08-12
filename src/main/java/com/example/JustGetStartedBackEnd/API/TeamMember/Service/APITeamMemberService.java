package com.example.JustGetStartedBackEnd.API.TeamMember.Service;

import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class APITeamMemberService {
    private final TeamMemberRepository teamMemberRepository;

    @Transactional(readOnly = true)
    public TeamMemberListDTO findMyTeam(Long memberId){
        List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(memberId);

        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        List<TeamMemberDTO> teamMemberDTOList = new ArrayList<>();
        for (TeamMember teamMember : teamMembers) {
            teamMemberDTOList.add(teamMember.toTeamMemberDTO());
        }
        teamMemberListDTO.setTeamMemberDTOList(teamMemberDTOList);
        return teamMemberListDTO;
    }
}
