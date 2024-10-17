package com.example.JustGetStartedBackEnd.API.FCM.Service;

import com.example.JustGetStartedBackEnd.API.FCM.Entity.FCMToken;
import com.example.JustGetStartedBackEnd.API.FCM.Repository.FCMRepository;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Service.APITeamService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {
    private final FCMRepository fcmRepository;
    private final MemberService memberService;
    private final APITeamService apiTeamService;

    @Transactional(rollbackFor=Exception.class)
    public void save(Long memberId, String token){
        Optional<FCMToken> optionalToken = fcmRepository.findByMemberId(memberId);
        if(optionalToken.isPresent()){
            optionalToken.get().updateToken(token);
            return;
        }

        FCMToken fcmToken = FCMToken.builder()
                .fcmToken(token)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .member(memberService.findByIdReturnEntity(memberId))
                .build();

        fcmRepository.save(fcmToken);
    }

    @Transactional(rollbackFor=Exception.class)
    public void sendMessage() throws FirebaseMessagingException {
        // FCMToken을 가져와서 리스트로 변환
        List<FCMToken> tokens = fcmRepository.findAll();
        List<String> fcmTokenList = tokens.stream()
                .map(FCMToken::getFcmToken)
                .toList();

        List<Team> teams = apiTeamService.findTop3Team();
        StringBuilder body = new StringBuilder();
        for(int i = 1; i < teams.size()+1; i++){
            body.append(String.valueOf(i))
                    .append("등 팀: ")
                    .append(teams.get(i-1).getTeamName())
                    .append("\n");
        }

        // 메시지 리스트 생성
        List<Message> messages = new ArrayList<>();
        for (String token : fcmTokenList) {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle("JGS 팀 순위")
                            .setBody(body.toString())
                            .build())
                    .setToken(token) // 각 토큰을 설정
                    .build();
            messages.add(message);
        }

        // 메시지 전송
        BatchResponse response = FirebaseMessaging.getInstance().sendEach(messages);
        // 실패한 토큰 확인
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(fcmTokenList.get(i));
                }
            }
            log.info("실패한 토큰 목록: {}", failedTokens);
        }

    }
}
