package com.example.JustGetStartedBackEnd.API.CommonNotification.Service;

import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.API.CommonNotification.DTO.Response.NotificationDTO;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification;
import com.example.JustGetStartedBackEnd.API.CommonNotification.ExceptionType.NotificationExceptionType;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.NotificationRepository;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APINotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private APINotificationService apiNotificationService;

    @Test
    @DisplayName("일반 알림 저장 - 성공")
    void saveNotification() {
        Member member = mock(Member.class);
        when(memberService.findByIdReturnEntity(1L)).thenReturn(member);

        apiNotificationService.saveNotification("content", 1L);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }


    @Test
    @DisplayName("일반 알림 읽음 처리 - 성공")
    void readNotification_Success() {
        Notification notification = mock(Notification.class);
        Member member = mock(Member.class);
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notification.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        apiNotificationService.readNotification(1L, 1L);
        verify(notificationRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("일반 알림 읽음 처리 (알림 찾기 실패) - 실패")
    void readNotification_Fail() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> apiNotificationService.readNotification(1L, 2L));

        assertEquals(NotificationExceptionType.NOTIFICATION_NOT_FOUND, exception.getExceptionType());
    }

    @Test
    @DisplayName("알림 삭제 - 성공")
    void deleteNotification() {
        Long notificationId = 1L;
        Long memberId = 1L;
        Notification notification = mock(Notification.class);
        Member member = mock(Member.class);
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notification.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);

        apiNotificationService.deleteNotification(memberId, notificationId);

        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

    @Test
    @DisplayName("알림 삭제 - 실패 (예외 발생)")
    void deleteNotification_Failure() {
        Long notificationId = 1L;
        Long memberId = 1L;
        Notification notification = mock(Notification.class);
        Member member = mock(Member.class);
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notification.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);
        doThrow(new RuntimeException("Delete failed")).when(notificationRepository).deleteById(notificationId);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            apiNotificationService.deleteNotification(memberId, notificationId);
        });

        assertEquals(NotificationExceptionType.NOTIFICATION_DELETE_ERROR, exception.getExceptionType());

        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

    @Test
    @DisplayName("일반 알림 전체 삭제 - 성공")
    void deleteAllNotification_Success() {
        Long memberId = 1L;

        apiNotificationService.deleteAllNotification(memberId);

        verify(notificationRepository, times(1)).deleteByMemberId(memberId);
    }

    @Test
    @DisplayName("일반 알림 전체 삭제 - 실패(예외 발생)")
    void deleteAllNotification_Fail() {
        Long memberId = 1L;
        doThrow(new RuntimeException("Delete failed")).when(notificationRepository).deleteByMemberId(memberId);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            apiNotificationService.deleteAllNotification(memberId);
        });

        assertEquals(NotificationExceptionType.NOTIFICATION_DELETE_ERROR, exception.getExceptionType());

        verify(notificationRepository, times(1)).deleteByMemberId(memberId);
    }

    @Test
    @DisplayName("멤버의 일반 알림 전체 조회 - 성공")
    void getAllNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationList.add(notificationDTO);
        when(notificationRepository.findByMemberId(anyLong())).thenReturn(notificationList);

        apiNotificationService.getAllNotification(anyLong());

        verify(notificationRepository, times(1)).findByMemberId(anyLong());
    }
}