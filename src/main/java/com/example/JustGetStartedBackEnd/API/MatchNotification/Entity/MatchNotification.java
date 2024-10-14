package com.example.JustGetStartedBackEnd.API.MatchNotification.Entity;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="match_notification")
@NoArgsConstructor
public class MatchNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_notifi_id")
    private Long matchNotifiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_post_id")
    private MatchPost matchPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appli_team_name")
    private Team applicantTeam;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "date")
    private LocalDateTime date;

    public MatchNotificationDTO toDTO() {
        MatchNotificationDTO matchNotificationDTO = new MatchNotificationDTO();
        matchNotificationDTO.setMatchPostId(this.getMatchPost().getMatchPostId());
        matchNotificationDTO.setMatchNotificationId(this.getMatchNotifiId());
        matchNotificationDTO.setRead(this.isRead());
        matchNotificationDTO.setContent(this.getContent());
        matchNotificationDTO.setTeamName(this.getApplicantTeam().getTeamName());
        matchNotificationDTO.setDate(this.date);
        return matchNotificationDTO;
    }

    public void updateRead(){
        this.isRead = true;
    }

    @Builder
    public MatchNotification(MatchPost matchPost, Team team, String content, boolean isRead, LocalDateTime date) {
        this.matchPost = matchPost;
        this.applicantTeam = team;
        this.content = content;
        this.isRead = isRead;
        this.date = date;
    }
}
