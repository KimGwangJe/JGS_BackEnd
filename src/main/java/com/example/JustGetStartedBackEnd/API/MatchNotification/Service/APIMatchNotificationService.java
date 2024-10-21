package com.example.JustGetStartedBackEnd.API.MatchNotification.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.SSEMessageDTO;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.CreateMatchDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.CreateMatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Request.MatchingDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.Response.MatchNotificationListDTO;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType.MatchNotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.MatchNotificationRepository;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class APIMatchNotificationService {
    private final MatchNotificationRepository matchNotificationRepository;
    private final TeamService teamService;
    private final MatchPostService matchPostService;
    private final APIMatchService matchService;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apinotificationService;

    private final ApplicationEventPublisher publisher;

    @Transactional(rollbackFor = Exception.class)
    public void createMatchNotification(Long memberId, CreateMatchNotificationDTO dto){
        String applicantTeamName = dto.getTeamName();
        //신청자의 팀
        Team team = teamService.findByTeamNameReturnEntity(applicantTeamName);
        apiTeamMemberService.validateLeaderAuthority(team, memberId);

        //이미 매치가 신청된 상태인지 확인
        MatchNotification matchNotification = matchNotificationRepository.findByMatchPostIdAndTeamName(dto.getMatchPostId(), applicantTeamName);
        if(matchNotification != null){
            //신청 한 이력이 있으면 에러
            log.warn("Match Notification Already Request");
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_ALREADY_REQUEST);
        }

        MatchPost matchPost = getValidatedMatchPost(dto);

        Long notificationMemberId = apiTeamMemberService.getLeaderId(matchPost.getTeamA());
        String message = applicantTeamName + "팀이 " +
                matchPost.getTeamA().getTeamName() + "팀에 매치를 신청하였습니다.";
        publisher.publishEvent(new SSEMessageDTO(notificationMemberId, message));

        MatchNotification newMatchNotification = MatchNotification.builder()
                .matchPost(matchPost)
                .team(team)
                .content(message)
                .isRead(false)
                .date(LocalDateTime.now())
                .build();

        try{
            matchNotificationRepository.save(newMatchNotification);
        } catch(Exception e){
            log.warn("Create Match Notification failed : {}", e.getMessage());
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_REQUEST_ERROR);
        }
    }

    //매칭이 성사되면 매칭 글까지 삭제 해버리면 됨
    @Transactional(rollbackFor = Exception.class)
    public void deleteMatchNotification(Long memberId, MatchingDTO matchingDTO){
        //매치 수락시 -> 매치 생성 매치글 삭제 매치 알림들 전부 삭제
        MatchNotification matchNotification = matchNotificationRepository.findById(matchingDTO.getMatchNotificationId())
                .orElseThrow(() -> new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_NOT_FOUND));

        MatchPost matchPost = matchNotification.getMatchPost();
        //그 팀의 리더만이 매치 수락 가능
        apiTeamMemberService.validateLeaderAuthority(matchPost.getTeamA(), memberId);

        //매치 요청을 보낸 팀의 리더의 ID를 가져옴
        Long notificationMemberId = apiTeamMemberService.getLeaderId(matchNotification.getApplicantTeam());

        //매치를 올린 팀
        String matchPostTeamName = matchPost.getTeamA().getTeamName();
        //도전 팀
        String challengeTeamName = matchNotification.getApplicantTeam().getTeamName();

        String message;

        try{
            //매치를 수락한 경우
            //매치를 요청 한 사람에게 알림
            if(matchingDTO.getStatus()){
                CreateMatchDTO createMatchDTO = new CreateMatchDTO();
                createMatchDTO.setMatchDate(Timestamp.valueOf(matchPost.getMatchDate()));
                createMatchDTO.setTeamA(matchPostTeamName); //매치를 올린 팀
                createMatchDTO.setTeamB(challengeTeamName); //도전자

                message = matchPostTeamName + "팀과 " +
                        challengeTeamName +
                        "팀의 매치가 성사 되었습니다.";

                Timestamp lastMatchDate =  Timestamp.valueOf(matchPost.getMatchDate());
                //두 팀의 마지막 매치 날짜 변경
                matchNotification.getMatchPost().getTeamA().updateLastMatchDate(lastMatchDate);
                matchNotification.getApplicantTeam().updateLastMatchDate(lastMatchDate);

                //매치 생성
                matchService.createMatch(createMatchDTO);
                //매치글에 대한 다른 알림들까지 전부 삭제
                matchNotificationRepository.deleteAllByMatchPostId(matchPost.getMatchPostId());

                //매치 포스트를 마감처리
                matchPost.updateIsEnd();
            } else {
                message = matchPostTeamName + "팀과 " +
                        challengeTeamName +
                        "팀의 매치가 상대팀의 팀장으로부터 거부되었습니다.";

                //try 필요
                matchNotificationRepository.deleteById(matchingDTO.getMatchNotificationId());
            }
            //매치 승인 / 거부 알림 SSO
            publisher.publishEvent(new SSEMessageDTO(notificationMemberId, message));
            apinotificationService.saveNotification(message, notificationMemberId);
        } catch(Exception e){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRead(Long matchNotificationId){
        MatchNotification matchNotification = matchNotificationRepository.findById(matchNotificationId)
                .orElseThrow(() -> new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_NOT_FOUND));
        matchNotification.updateRead();
    }

    @Transactional(readOnly = true)
    public MatchNotificationListDTO getAllMatchNotifications(Long memberId) {
        //사용자가 속한 모든 팀을 가져옴
        TeamMemberListDTO teamMemberListDTO = apiTeamMemberService.findMyTeam(memberId);

        //사용자가 속한 팀 중 사용자가 리더인 팀만 남김
        List<TeamMemberDTO> leaderTeams = teamMemberListDTO.getTeamMemberDTOList().stream()
                .filter(member -> member.getRole() == TeamMemberRole.Leader)
                .toList();

        // teamName 리스트 추출
        List<String> teamNames = leaderTeams.stream()
                .map(TeamMemberDTO::getTeamName)
                .collect(Collectors.toList());

        // 한 번의 쿼리로 모든 teamName에 대한 데이터를 조회
        List<MatchNotificationDTO> matchNotificationDTOList = matchNotificationRepository.findByTeamNameIn(teamNames);


        MatchNotificationListDTO matchNotificationListDTO = new MatchNotificationListDTO();
        matchNotificationListDTO.setMatchNotificationDTOList(matchNotificationDTOList);
        return matchNotificationListDTO;
    }

    private MatchPost getValidatedMatchPost(CreateMatchNotificationDTO dto){
        MatchPost matchPost = matchPostService.findMatchPostById(dto.getMatchPostId());
        if(matchPost.getMatchDate().isBefore(LocalDateTime.now())){
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_INVALID_DATE);
        }
        if(matchPost.isEnd()){
            log.warn("Match Post Is Already End");
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_POST_IS_END);
        }
        if(matchPost.getTeamA().getTeamName().equals(dto.getTeamName())){
            log.warn("Can Not Request Same Team");
            throw new BusinessLogicException(MatchNotificationExceptionType.SAME_TEAM_MATCH_ERROR);
        }
        return matchPost;
    }
}
