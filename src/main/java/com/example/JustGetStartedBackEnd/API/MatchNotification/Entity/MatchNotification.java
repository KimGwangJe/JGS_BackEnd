package com.example.JustGetStartedBackEnd.API.MatchNotification.Entity;

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
    private int matchNotifiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_post_id")
    private MatchPost matchPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appli_team_name")
    private Team appliTeamName;

    @Builder
    public MatchNotification(MatchPost matchPost, Team team) {
        this.matchPostId = matchPost;
        this.appliTeamName = team;
    }
}
