package com.example.JustGetStartedBackEnd.Domain;

import com.example.JustGetStartedBackEnd.Member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="join_notification")
@NoArgsConstructor
public class JoinNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "is_join")
    private boolean isJoin;

    @Column(name = "is_read")
    private boolean isRead;

    @JoinColumn(name = "pub_member")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member pubMember;

    @JoinColumn(name = "community_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    @Builder
    JoinNotification(boolean isJoin, boolean isRead, Member pubMember, Community community) {
        this.isJoin = isJoin;
        this.isRead = isRead;
        this.pubMember = pubMember;
        this.community = community;
    }
}
