package com.example.JustGetStartedBackEnd.API.Match.Entity;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchPagingDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TierDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "game_match")
@NoArgsConstructor
public class GameMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "match_date")
    private Timestamp matchDate;

    @Column(name = "team_a_score")
    private int teamAScore;

    @Column(name = "team_b_score")
    private int teamBScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a")
    private Team teamA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b")
    private Team teamB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee")
    private Member referee;

    public void updateTeamAScore(int teamAScore) {
        this.teamAScore = teamAScore;
    }

    public void updateTeamBScore(int teamBScore) {
        this.teamBScore = teamBScore;
    }

    @Builder
    public GameMatch(Timestamp matchDate, Team teamA, Team teamB, Member referee) {
        this.matchDate = matchDate;
        this.teamA = teamA;
        this.teamB = teamB;
        this.referee = referee;
    }

    public MatchDTO toMatchDTO() {
        MatchDTO dto = new MatchDTO();
        dto.setMatchId(this.matchId);
        dto.setMatchDate(this.matchDate);
        dto.setTeamA(this.teamA.getTeamName());
        dto.setTeamB(this.teamB.getTeamName());
        dto.setTeamAScore(this.teamAScore);
        dto.setTeamBScore(this.teamBScore);
        dto.setReferee(this.referee == null ? null : this.referee.getMemberId());
        return dto;
    }

    public MatchPagingDTO toMatchPagingDTO() {
        MatchPagingDTO dto = new MatchPagingDTO();
        dto.setMatchId(this.matchId);
        dto.setMatchDate(this.matchDate);
        dto.setTeamA(this.teamA.getTeamName());
        dto.setTeamB(this.teamB.getTeamName());
        dto.setTeamAScore(this.teamAScore);
        dto.setTeamBScore(this.teamBScore);
        dto.setReferee(this.referee == null ? null : this.referee.getMemberId());
        dto.setTeamATier(toATierDTO());
        dto.setTeamBTier(toBTierDTO());
        return dto;
    }

    public TierDTO toATierDTO(){
        TierDTO dto = new TierDTO();
        dto.setTierId(this.teamA.getTier().getTierId());
        dto.setTierName(this.teamA.getTier().getTierName());
        return dto;
    }

    public TierDTO toBTierDTO(){
        TierDTO dto = new TierDTO();
        dto.setTierId(this.teamB.getTier().getTierId());
        dto.setTierName(this.teamB.getTier().getTierName());
        return dto;
    }
}
