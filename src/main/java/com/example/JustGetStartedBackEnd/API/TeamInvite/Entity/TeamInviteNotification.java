package com.example.JustGetStartedBackEnd.API.TeamInvite.Entity;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamInvite.DTO.TeamInviteInfoDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "team_invite_notification")
@NoArgsConstructor
public class TeamInviteNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long inviteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "invite_date")
    private LocalDateTime inviteDate;

    @Column(name = "content")
    private String content;

    public void updateRead() {
        this.isRead = true;
    }

    public TeamInviteInfoDTO toDTO() {
        TeamInviteInfoDTO teamInviteInfoDTO = new TeamInviteInfoDTO();
        teamInviteInfoDTO.setInviteId(this.getInviteId());
        teamInviteInfoDTO.setTeamName(this.getTeam().getTeamName());
        teamInviteInfoDTO.setInviteDate(this.getInviteDate());
        teamInviteInfoDTO.setRead(this.isRead());
        return teamInviteInfoDTO;
    }

    @Builder
    TeamInviteNotification(Team team, Member member, boolean status, boolean isRead, LocalDateTime inviteDate, String content) {
        this.team = team;
        this.member = member;
        this.isRead = isRead;
        this.inviteDate = inviteDate;
        this.content = content;
    }
}
