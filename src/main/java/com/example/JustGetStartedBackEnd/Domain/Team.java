package com.example.JustGetStartedBackEnd.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor
public class Team {

    @Id
    @Column(name = "team_name")
    private String teamName;

    @NotBlank
    @Column(name = "create_date")
    private Date createDate;

    @NotBlank
    @Column(name = "tier")
    private String tier;

    @Column(name = "tier_point")
    private int tierPoint;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "last_match_date")
    private Date lastMatchDate;

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Community> communities = new ArrayList<>();

    @OneToMany(mappedBy = "winnerTeam", fetch = FetchType.LAZY)
    private List<Conference> conferences = new ArrayList<>();

    @OneToMany(mappedBy = "teamA", fetch = FetchType.LAZY)
    private List<GameMatch> gameMatchesAsTeamA = new ArrayList<>();

    @OneToMany(mappedBy = "teamB", fetch = FetchType.LAZY)
    private List<GameMatch> gameMatchesAsTeamB = new ArrayList<>();

    @OneToMany(mappedBy = "teamA", fetch = FetchType.LAZY)
    private List<MatchPost> matchPostsAsTeamA = new ArrayList<>();

    @OneToMany(mappedBy = "appliTeamName", fetch = FetchType.LAZY)
    private List<MatchNotification> matchNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamReview> teamReviews = new ArrayList<>();

    public void updateLastMatchDate(Date lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateTierPoint(int point) {
        this.tierPoint += point;
    }
}
