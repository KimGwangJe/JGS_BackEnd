package com.example.JustGetStartedBackEnd.API.MatchPost.Entity;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.Domain.MatchNotification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "match_post")
@NoArgsConstructor
public class MatchPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_post_id")
    private Long matchPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a")
    private Team teamA;

    @Column(name = "is_end")
    private boolean isEnd;

    @Column(name = "match_date")
    private Date matchDate;

    @Column(name = "location")
    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "matchPostId", fetch = FetchType.LAZY)
    private List<MatchNotification> matchNotifications;

    public void updateIsEnd() {
        this.isEnd = true;
    }

    public MatchPostDTO toMatchPostDTO(){
        MatchPostDTO matchPostDTO = new MatchPostDTO();
        matchPostDTO.setMatchPostId(this.matchPostId);
        matchPostDTO.setTeamName(this.teamA.getTeamName());
        matchPostDTO.setMatchDate(this.matchDate);
        matchPostDTO.setLocation(this.location);
        matchPostDTO.setEnd(this.isEnd);
        return matchPostDTO;
    }

    @Builder
    public MatchPost(Team teamA, boolean isEnd, Date matchDate, String location) {
        this.teamA = teamA;
        this.isEnd = isEnd;
        this.matchDate = matchDate;
        this.location = location;
    }
}