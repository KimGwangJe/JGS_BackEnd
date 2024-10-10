package com.example.JustGetStartedBackEnd.API.CommonNotification.Service;

import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Request.NotificationDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Request.NotificationListDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification;
import com.example.JustGetStartedBackEnd.API.CommonNotification.ExceptionType.NotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.NotificationRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class APINotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void saveNotification(String content, Long memberId){
        Notification notification = Notification.builder()
                .content(content)
                .member(memberService.findByIdReturnEntity(memberId))
                .isRead(false)
                .date(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(rollbackFor = Exception.class)
    public void readNotification(Long memberId, Long notificationId){
        Notification notification = getNotificationById(notificationId);

        validMemberId(memberId, notification);

        notification.updateIsRead();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNotification(Long memberId, Long notificationId) {
        Notification notification = getNotificationById(notificationId);

        validMemberId(memberId, notification);

        try {
            notificationRepository.deleteById(notificationId);
        } catch (Exception e) {
            log.warn("Notification Delete Failed : {}", e.getMessage());
            throw new BusinessLogicException(NotificationExceptionType.NOTIFICATION_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAllNotification(Long memberId) {
        try {
            notificationRepository.deleteByMemberId(memberId);
        } catch (Exception e) {
            log.warn("All Notification Delete Failed : {}", e.getMessage());
            throw new BusinessLogicException(NotificationExceptionType.NOTIFICATION_DELETE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public NotificationListDTO getAllNotification(Long memberId){
        // 회원 ID로 알림 목록 조회
        List<Notification> notificationList = notificationRepository.findByMemberId(memberId);

        // 스트림을 사용하여 DTO로 변환
        List<NotificationDTO> notificationDTOList = notificationList.stream()
                .map(Notification::toDTO)
                .collect(Collectors.toList());

        // 결과를 NotificationListDTO에 설정
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        notificationListDTO.setNotificationDTOList(notificationDTOList);

        return notificationListDTO;
    }

    private Notification getNotificationById(Long notificationId){
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessLogicException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }

    private void validMemberId(Long memberId, Notification notification){
        if(!Objects.equals(notification.getMember().getMemberId(), memberId)){
            throw new BusinessLogicException(MemberExceptionType.MEMBER_INVALID_AUTHORITY);
        }
    }
}
