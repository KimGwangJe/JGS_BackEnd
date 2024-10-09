package com.example.JustGetStartedBackEnd.API.TeamReview.Entity;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.TeamReviewDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="team_review")
@NoArgsConstructor
public class TeamReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_review_id")
    private Long teamReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_name")
    private Team team;

    @Column(name="rating")
    private float rating;

    @Column(name="content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writter")
    private Member writter;

    @Builder
    TeamReview(Team team, float rating, String content, Member writter) {
        this.team = team;
        this.rating = rating;
        this.content = content;
        this.writter = writter;
    }

    public TeamReviewDTO toTeamReviewDTO() {
        TeamReviewDTO dto = new TeamReviewDTO();
        dto.setTeamName(this.team.getTeamName());
        dto.setTeamReviewID(this.teamReviewId);
        dto.setContent(this.content);
        dto.setWritter(this.writter.getMemberId());
        dto.setRating(this.rating);
        return dto;
    }
}
