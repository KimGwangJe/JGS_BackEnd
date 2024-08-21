package com.example.JustGetStartedBackEnd.API.MatchNotification.Service;

import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchingDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType.MatchNotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.MatchNotificationRepository;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.SSE.Controller.NotificationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class APIMatchNotificationService {
    private final MatchNotificationRepository matchNotificationRepository;
    private final TeamService teamService;
    private final MatchPostService matchPostService;
    private final NotificationController notificationController;
    private final APIMatchService matchService;
    private final APITeamMemberService apiTeamMemberService;

    @Transactional(rollbackFor = Exception.class)
    public void createMatchNotification(Long memberId, CreateMatchNotificationDTO dto){
        //신청자의 팀
        Team team = teamService.findByTeamNameReturnEntity(dto.getTeamName());
        boolean isLeader = apiTeamMemberService.isLeader(team, memberId);
        if(!isLeader) throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);

        //이미 매치가 신청된 상태인지 확인
        MatchNotification matchNotification = matchNotificationRepository.findByMatchPostIdAndTeamName(dto.getMatchPostId(), dto.getTeamName());
        if(matchNotification != null){
            //신청 한 이력이 있으면 에러
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_ALREADY_REQUEST);
        }

        // 매치 글에 등록된 매치 날짜보다 이후라면 신청 불가
        MatchPost matchPost = matchPostService.findMatchPostById(dto.getMatchPostId());
        if(matchPost.getMatchDate().isBefore(LocalDateTime.now())){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_INVALID_DATE);
        }
        if(matchPost.isEnd()){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_POST_IS_END);
        }
        if(matchPost.getTeamA().getTeamName().equals(dto.getTeamName())){
            throw new BusinessLogicException(MatchNotificationExceptionType.SAME_TEAM_MATCH_ERROR);
        }

        MatchNotification newMatchNotification = MatchNotification.builder()
                .matchPost(matchPost)
                .team(team)
                .build();

        try{
            Optional<TeamMember> optionalLeader = matchPost.getTeamA().getTeamMembers().stream()
                    .filter(teamMember -> teamMember.getRole() == TeamMemberRole.Leader)
                    .findFirst(); // 첫 번째 일치하는 요소를 반환
            if(optionalLeader.isPresent()){
                TeamMember leader = optionalLeader.get();
                Long notificationMemberId = leader.getMember().getMemberId();
                notificationController.sendNotification(notificationMemberId,
                        dto.getTeamName() + "팀이 " +
                                matchPost.getTeamA().getTeamName() +
                                "팀에 매치를 신청하얐습니다.");
                matchNotificationRepository.save(newMatchNotification);
            } else {
                throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_NOT_FOUND);
            }
        } catch(Exception e){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_REQUEST_ERROR);
        }
    }

    //매칭이 성사되면 매칭 글까지 삭제 해버리면 됨
    @Transactional(rollbackFor = Exception.class)
    public void deleteMatchNotification(Long memberId, MatchingDTO matchingDTO){
        //매치 수락시 -> 매치 생성 매치글 삭제 매치 알림들 전부 삭제
        MatchNotification matchNotification = matchNotificationRepository.findById(matchingDTO.getMatchNotificationId())
                .orElseThrow(() -> new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_NOT_FOUND));

        //그 팀의 리더만이 매치 수락 가능
        boolean isLeader = apiTeamMemberService.isLeader(matchNotification.getMatchPostId().getTeamA(), memberId);
        if(!isLeader) throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);

        //매치를 수락한 경우
        if(matchingDTO.getStatus()){
            CreateMatchDTO createMatchDTO = new CreateMatchDTO();
            createMatchDTO.setMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));
            createMatchDTO.setTeamA(matchNotification.getMatchPostId().getTeamA().getTeamName()); //매치를 올린 팀
            createMatchDTO.setTeamB(matchNotification.getAppliTeamName().getTeamName()); //도전자

            //두 팀의 마지막 매치 날짜 변경
            matchNotification.getMatchPostId().getTeamA().updateLastMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));
            matchNotification.getAppliTeamName().updateLastMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));

            matchService.createMatch(createMatchDTO);

            matchNotificationRepository.deleteAllByMatchPostId(matchNotification.getMatchPostId().getMatchPostId());

            //매치 포스트를 마감처리
            matchNotification.getMatchPostId().updateIsEnd();
        }

        //매치 수락과 별개로 매치 알림을 삭제
        try{
            matchNotificationRepository.deleteById(matchingDTO.getMatchNotificationId());
        } catch(Exception e){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_DELETE_ERROR);
        }
    }
}
