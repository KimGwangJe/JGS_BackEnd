package com.example.JustGetStartedBackEnd.API.MatchNotification.Service;

import com.example.JustGetStartedBackEnd.API.Match.Service.APIMatchService;
import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.*;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.ExceptionType.MatchNotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.MatchNotificationRepository;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Notification.Service.APINotificationService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMemberRole;
import com.example.JustGetStartedBackEnd.API.TeamMember.ExceptionType.TeamMemberExceptionType;
import com.example.JustGetStartedBackEnd.API.TeamMember.Service.APITeamMemberService;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.SSE.Controller.NotificationController;
import com.example.JustGetStartedBackEnd.SSE.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final NotificationService notificationService;
    private final APIMatchService matchService;
    private final APITeamMemberService apiTeamMemberService;
    private final APINotificationService apinotificationService;

    @Transactional(rollbackFor = Exception.class)
    public void createMatchNotification(Long memberId, CreateMatchNotificationDTO dto){
        //신청자의 팀
        Team team = teamService.findByTeamNameReturnEntity(dto.getTeamName());
        boolean isLeader = apiTeamMemberService.isLeader(team, memberId);
        if(!isLeader) {
            log.warn("Not Allow Authority - Create Match Notification");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }

        //이미 매치가 신청된 상태인지 확인
        MatchNotification matchNotification = matchNotificationRepository.findByMatchPostIdAndTeamName(dto.getMatchPostId(), dto.getTeamName());
        if(matchNotification != null){
            //신청 한 이력이 있으면 에러
            log.warn("Match Notification Already Request");
            throw new BusinessLogicException(MatchNotificationExceptionType.MATCH_NOTIFICATION_ALREADY_REQUEST);
        }

        // 매치 글에 등록된 매치 날짜보다 이후라면 신청 불가
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

        try{
            Long notificationMemberId = apiTeamMemberService.getLeaderId(matchPost.getTeamA());
            String message = dto.getTeamName() + "팀이 " +
                    matchPost.getTeamA().getTeamName() + "팀에 매치를 신청하였습니다.";
            notificationService.sendNotification(notificationMemberId, message);

            MatchNotification newMatchNotification = MatchNotification.builder()
                    .matchPost(matchPost)
                    .team(team)
                    .content(message)
                    .isRead(false)
                    .date(LocalDateTime.now())
                    .build();

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

        //그 팀의 리더만이 매치 수락 가능
        boolean isLeader = apiTeamMemberService.isLeader(matchNotification.getMatchPostId().getTeamA(), memberId);
        if(!isLeader) {
            log.warn("Not Allow Authority - Delete Match Notification");
            throw new BusinessLogicException(TeamMemberExceptionType.TEAM_MEMBER_INVALID_AUTHORITY);
        }

        //매치 요청을 보낸 팀의 리더의 ID를 가져옴
        Long notificationMemberId = apiTeamMemberService.getLeaderId(matchNotification.getAppliTeamName());

        //매치를 수락한 경우
        //매치를 요청 한 사람에게 알림
        if(matchingDTO.getStatus()){
            CreateMatchDTO createMatchDTO = new CreateMatchDTO();
            createMatchDTO.setMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));
            createMatchDTO.setTeamA(matchNotification.getMatchPostId().getTeamA().getTeamName()); //매치를 올린 팀
            createMatchDTO.setTeamB(matchNotification.getAppliTeamName().getTeamName()); //도전자

            String message = matchNotification.getMatchPostId().getTeamA().getTeamName() + "팀과 " +
                    matchNotification.getAppliTeamName().getTeamName() +
                    "팀의 매치가 성사 되었습니다.";
            //매치 성사 알림 SSO & DB 저장
            notificationService.sendNotification(notificationMemberId, message);
            apinotificationService.saveNotification(message, notificationMemberId);

            //두 팀의 마지막 매치 날짜 변경
            matchNotification.getMatchPostId().getTeamA().updateLastMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));
            matchNotification.getAppliTeamName().updateLastMatchDate(Timestamp.valueOf(matchNotification.getMatchPostId().getMatchDate()));

            //매치 생성
            matchService.createMatch(createMatchDTO);

            //매치글에 대한 다른 알림들까지 전부 삭제
            matchNotificationRepository.deleteAllByMatchPostId(matchNotification.getMatchPostId().getMatchPostId());

            //매치 포스트를 마감처리
            matchNotification.getMatchPostId().updateIsEnd();
        } else {
            String message = matchNotification.getMatchPostId().getTeamA().getTeamName() + "팀과 " +
                    matchNotification.getAppliTeamName().getTeamName() +
                    "팀의 매치가 상대팀의 팀장으로부터 거부되었습니다.";
            //매치 거부 알림 SSO & DB 저장
            notificationService.sendNotification(notificationMemberId, message);
            apinotificationService.saveNotification(message, notificationMemberId);

            matchNotificationRepository.deleteById(matchingDTO.getMatchNotificationId());
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

        // DTO 변환
        List<MatchNotificationDTO> matchNotificationDTOList = leaderTeams.stream()
                .flatMap(teamMemberDTO -> matchNotificationRepository.findByTeamName(teamMemberDTO.getTeamName()).stream())
                .map(MatchNotification::toDTO)
                .collect(Collectors.toList());


        MatchNotificationListDTO matchNotificationListDTO = new MatchNotificationListDTO();
        matchNotificationListDTO.setMatchNotificationDTOList(matchNotificationDTOList);
        return matchNotificationListDTO;
    }
}
