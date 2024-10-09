package com.example.JustGetStartedBackEnd.API.TeamMember.Service;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.TeamMemberRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class APITeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamService teamService;
    private final MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void createLeaderTeamMember(Member member, Team team){
        try{
            TeamMember teamMember = TeamMember.builder()
                    .member(member)
                    .team(team)
                    .role(TeamMemberRole.Leader)
                    .build();

            teamMemberRepository.save(teamMember);
        } catch(Exception e){
            log.warn("Team Leader Save Error : {}", e.getMessage());
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_SAVE_ERROR);
        }
    }

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
            if(Objects.equals(teamMember.getMember().getMemberId(), requestMember.getMember().getMemberId())){
                log.warn("Can Not Delete Leader Of Team");
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_DELETE_ME);
            }
            if(requestMember.getTeam().getTeamName().equals(teamMember.getTeam().getTeamName())
                    && requestMember.getRole().equals(TeamMemberRole.Leader)){
                teamMemberRepository.delete(teamMember);
                return;
            }
        }
        log.warn("Not Allow Authority - Delete Team Member");
        throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
    }

    @Transactional(rollbackFor = Exception.class)
    public void joinTeamMember(Long memberId, String teamName){
        Team team = teamService.findByTeamNameReturnEntity(teamName);
        Member member = memberService.findByIdReturnEntity(memberId);

        for(TeamMember teamMember :team.getTeamMembers()){
            if(Objects.equals(teamMember.getMember().getMemberId(), member.getMemberId())){
                log.warn("Team Member Already Join");
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_ALREADY_JOIN);
            }
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .role(TeamMemberRole.Member)
                .build();
        try{
            teamMemberRepository.save(teamMember);
        } catch(Exception e){
            log.warn("Team Member Save Error : {}", e.getMessage());
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_SAVE_ERROR);
        }
    }

    public boolean isLeader(Team team, Long memberId){
        return team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().getMemberId().equals(memberId) &&
                        teamMember.getRole() == TeamMemberRole.Leader);
    }

    public Long getLeaderId(Team team){
        Optional<TeamMember> optionalLeader = team.getTeamMembers().stream()
                .filter(teamMember -> teamMember.getRole() == TeamMemberRole.Leader)
                .findFirst(); // 첫 번째 일치하는 요소를 반환
        if(optionalLeader.isPresent()){
            TeamMember leader = optionalLeader.get();
            return leader.getMember().getMemberId();
        } else {
            log.warn("Not Allow Authority - Get Leader Id");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }
    }
}
