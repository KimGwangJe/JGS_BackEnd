package com.example.JustGetStartedBackEnd.API.Notification.Entity;

import com.example.JustGetStartedBackEnd.API.Notification.DTO.NotificationDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "date")
    private LocalDateTime date;

    public void updateIsRead(){
        this.isRead = true;
    }

    public NotificationDTO toDTO() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationId(this.getNotificationId());
        notificationDTO.setRead(this.isRead());
        notificationDTO.setContent(this.getContent());
        notificationDTO.setDate(this.getDate());
        return notificationDTO;
    }

    @Builder
    Notification(String content, boolean isRead, Member member, LocalDateTime date) {
        this.content = content;
        this.isRead = isRead;
        this.member = member;
        this.date = date;
    }
}
