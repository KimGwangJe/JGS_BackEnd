package com.example.JustGetStartedBackEnd.API.Common.EventListener;

import com.example.JustGetStartedBackEnd.API.Common.DTO.FCMMessageDTO;
import com.example.JustGetStartedBackEnd.API.FCM.FCMDTO;
import com.example.JustGetStartedBackEnd.API.FCM.Service.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMEventListener {
    private final FCMService fcmService;

    // 매개변수에 이벤트 클래스를 정의하면 해ㄹ당 이벤트가 발생했을 때 수신해서 처리를 할 수 있다.
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendFCMNotification(FCMMessageDTO dto) throws FirebaseMessagingException {
        log.info("memberId : {}, message : {}", dto.memberId(), dto.message());
        fcmService.sendMessageForOneMember(dto.memberId(), dto.message());
    }

    @EventListener
    public void sendFCMNotificationForAllMember(FCMDTO fcmdto) throws FirebaseMessagingException {
        fcmService.sendMessageForAllMember();
    }
}
