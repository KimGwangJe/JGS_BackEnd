package com.example.JustGetStartedBackEnd.API.TeamMember.Service;

import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.TeamMemberRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
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

    @Transactional(rollbackFor = Exception.class)
    public void deleteTeamMember(Long memberId,Long teamMemberId){

        //삭제 대상
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId).orElseThrow(
                () -> new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_NOT_FOUND));

        //요청한 사람의 팀
        List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(memberId);

        // 팀원 삭제 요청을 보낸 사람이 그 팀의 리더여야됨
        for(TeamMember requestMember : teamMembers){
            //만약 자기자신을 방출 하려는 경우 에러
            if(teamMember.getMember().getMemberId() == requestMember.getMember().getMemberId()){
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_DELETE_ME);
            }
            if(requestMember.getTeam().getTeamName().equals(teamMember.getTeam().getTeamName())
                    && requestMember.getRole().equals(TeamMemberRole.Leader)){
                teamMemberRepository.delete(teamMember);
                return;
            }
        }
        throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }
}
