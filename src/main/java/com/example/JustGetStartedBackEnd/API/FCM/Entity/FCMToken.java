package com.example.JustGetStartedBackEnd.API.FCM.Entity;

import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "fcm_token")
@NoArgsConstructor
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "fcm_token", nullable = false, unique = true)
    private String fcmToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public FCMToken(Member member, String fcmToken, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.member = member;
        this.fcmToken = fcmToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateToken(String token){
        this.fcmToken = token;
        this.updatedAt = LocalDateTime.now();
    }
}
