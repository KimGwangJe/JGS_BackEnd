package com.example.JustGetStartedBackEnd.API.Common;

import com.example.JustGetStartedBackEnd.API.Common.DTO.SSEMessageDTO;
import com.example.JustGetStartedBackEnd.API.SSE.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SSEEventListener {
    private final NotificationService notificationService;

    // 매개변수에 이벤트 클래스를 정의하면 해ㄹ당 이벤트가 발생했을 때 수신해서 처리를 할 수 있다.
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSSENotification(SSEMessageDTO dto) {
        log.info("memberId : {}, message : {}", dto.getMemberId(), dto.getMessage());
        notificationService.sendNotification(dto.getMemberId(), dto.getMessage());
    }
}
