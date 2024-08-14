package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
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
    private Long notificationId;

    @Column(name = "is_read")
    private boolean isRead;

    @JoinColumn(name = "pub_member")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member pubMember;

    @JoinColumn(name = "community_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    public void updateRead(){
        this.isRead = true;
    }

    @Builder
    JoinNotification(boolean isRead, Member pubMember, Community community) {
        this.isRead = isRead;
        this.pubMember = pubMember;
        this.community = community;
    }
}
