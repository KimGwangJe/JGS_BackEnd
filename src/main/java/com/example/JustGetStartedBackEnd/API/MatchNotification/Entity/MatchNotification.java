package com.example.JustGetStartedBackEnd.API.MatchNotification.Entity;

import com.example.JustGetStartedBackEnd.API.MatchNotification.DTO.MatchNotificationDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private MatchPost matchPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appli_team_name")
    private Team appliTeamName;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    public MatchNotificationDTO toDTO() {
        MatchNotificationDTO matchNotificationDTO = new MatchNotificationDTO();
        matchNotificationDTO.setMatchPostId(this.getMatchPostId().getMatchPostId());
        matchNotificationDTO.setMatchNotificationId(this.getMatchNotifiId());
        matchNotificationDTO.setRead(this.isRead());
        matchNotificationDTO.setContent(this.getContent());
        matchNotificationDTO.setTeamName(this.getAppliTeamName().getTeamName());
        return matchNotificationDTO;
    }

    public void updateRead(){
        this.isRead = true;
    }

    @Builder
    public MatchNotification(MatchPost matchPost, Team team, String content, boolean isRead) {
        this.matchPostId = matchPost;
        this.appliTeamName = team;
        this.content = content;
        this.isRead = isRead;
    }
}
