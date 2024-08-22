package com.example.JustGetStartedBackEnd.API.Notification.Service;

import com.example.JustGetStartedBackEnd.API.Notification.Entity.Notification;
import com.example.JustGetStartedBackEnd.API.Notification.Repository.NotificationRepository;
import com.example.JustGetStartedBackEnd.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class APINotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void saveNotification(String content, Long memberId){
        Notification notification = Notification.builder()
                .content(content)
                .member(memberService.findByIdReturnEntity(memberId))
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
}
