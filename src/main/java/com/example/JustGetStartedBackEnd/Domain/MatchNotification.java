package com.example.JustGetStartedBackEnd.Domain;

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

    @Column(name = "status")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appli_team_name")
    private Team appliTeamName;

    public void updateStatus() {
        this.status = true;
    }

    @Builder
    public MatchNotification(MatchPost matchPostId, boolean status, Team appliTeamName) {
        this.matchPostId = matchPostId;
        this.status = status;
        this.appliTeamName = appliTeamName;
    }
}
