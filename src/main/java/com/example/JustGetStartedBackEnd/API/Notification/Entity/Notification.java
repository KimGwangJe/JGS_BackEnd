package com.example.JustGetStartedBackEnd.API.Notification.Entity;

import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateIsRead(){
        this.isRead = true;
    }

    @Builder
    Notification(String content, boolean isRead, Member member) {
        this.content = content;
        this.isRead = isRead;
        this.member = member;
    }
}
