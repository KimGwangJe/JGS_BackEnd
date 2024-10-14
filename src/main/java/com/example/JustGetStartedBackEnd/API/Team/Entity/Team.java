package com.example.JustGetStartedBackEnd.API.Team.Entity;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Response.ConferenceListDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.Response.MatchListDTO;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Response.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.Response.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.TeamReviewDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.TeamReviewListDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor
public class Team {

    @Id
    @Column(name = "team_name")
    private String teamName;

    @NotNull
    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id")
    private Tier tier;

    @Column(name = "tier_point")
    private int tierPoint;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "last_match_date")
    private Date lastMatchDate;

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Community> communities;

    @OneToMany(mappedBy = "winnerTeam", fetch = FetchType.LAZY)
    private List<Conference> conferences;

    @OneToMany(mappedBy = "teamA", fetch = FetchType.LAZY)
    private List<GameMatch> gameMatchesAsTeamA;

    @OneToMany(mappedBy = "teamB", fetch = FetchType.LAZY)
    private List<GameMatch> gameMatchesAsTeamB;

    @OneToMany(mappedBy = "teamA", fetch = FetchType.LAZY)
    private List<MatchPost> matchPostsAsTeamA;

    @OneToMany(mappedBy = "applicantTeam", fetch = FetchType.LAZY)
    private List<MatchNotification> matchNotifications;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamReview> teamReviews;

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamInviteNotification> teamInviteNotifications;

    public void updateLastMatchDate(Date lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateTier(Tier tier, int tierPoint){
        this.tier = tier;
        this.tierPoint = tierPoint;
    }

    public TeamDTO toTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName(this.teamName);
        teamDTO.setTier(this.tier.tierDTO());
        teamDTO.setCreateDate(this.createDate);
        teamDTO.setTierPoint(this.tierPoint);
        teamDTO.setIntroduce(this.introduce);
        teamDTO.setLastMatchDate(this.lastMatchDate);
        return teamDTO;
    }

    public TeamInfoDTO toTeamInfoDTO() {
        TeamInfoDTO teamInfoDTO = new TeamInfoDTO();
        teamInfoDTO.setTeamName(this.teamName);
        teamInfoDTO.setTier(this.tier.tierDTO());
        teamInfoDTO.setCreateDate(this.createDate);
        teamInfoDTO.setTierPoint(this.tierPoint);
        teamInfoDTO.setIntroduce(this.introduce);
        teamInfoDTO.setLastMatchDate(this.lastMatchDate);

        List<TeamMemberDTO> teamMemberDTOS = this.teamMembers.stream()
                .map(TeamMember::toTeamMemberDTO)
                .collect(Collectors.toList());
        TeamMemberListDTO teamMemberListDTO = new TeamMemberListDTO();
        teamMemberListDTO.setTeamMemberDTOList(teamMemberDTOS);
        teamInfoDTO.setTeamMemberListDTO(teamMemberListDTO);

        List<TeamReviewDTO> teamReviewDTOS = this.teamReviews.stream()
                .map(TeamReview::toTeamReviewDTO)
                .collect(Collectors.toList());
        TeamReviewListDTO teamReviewListDTO = new TeamReviewListDTO();
        teamReviewListDTO.setTeamReviewDTOList(teamReviewDTOS);
        teamInfoDTO.setTeamReviewListDTO(teamReviewListDTO);

        List<MatchDTO> matchDTOS = Stream.concat(
                        this.gameMatchesAsTeamA.stream(),
                        this.gameMatchesAsTeamB.stream()
                )
                .map(GameMatch::toMatchDTO)
                .collect(Collectors.toList());
        MatchListDTO matchListDTO = new MatchListDTO();
        matchListDTO.setMatches(matchDTOS);
        teamInfoDTO.setMatchListDTO(matchListDTO);

        List<ConferenceDTO> conferenceDTOS = this.conferences.stream()
                .map(Conference::toConferenceDTO)
                .collect(Collectors.toList());
        ConferenceListDTO conferenceListDTO = new ConferenceListDTO();
        conferenceListDTO.setConferenceDTOList(conferenceDTOS);
        teamInfoDTO.setConferenceListDTO(conferenceListDTO);

        return teamInfoDTO;
    }

    @Builder
    public Team(String teamName, Date createDate, Tier tier, int tierPoint, String introduce, Date lastMatchDate) {
        this.teamName = teamName;
        this.createDate = createDate;
        this.tier = tier;
        this.tierPoint = tierPoint;
        this.introduce = introduce;
        this.lastMatchDate = lastMatchDate;
    }
}
