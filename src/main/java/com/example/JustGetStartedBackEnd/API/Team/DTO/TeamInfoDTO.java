package com.example.JustGetStartedBackEnd.API.Team.DTO;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceListDTO;
import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchListDTO;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberListDTO;
import com.example.JustGetStartedBackEnd.API.TeamReview.DTO.TeamReviewListDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TeamInfoDTO {
    private String teamName;
    private Date createDate;
    private String tier;
    private int tierPoint;
    private String introduce;
    private Date lastMatchDate;

    //팀 회원
    private TeamMemberListDTO teamMemberListDTO;
    //팀 리뷰
    private TeamReviewListDTO teamReviewListDTO;
    //매치
    private MatchListDTO matchListDTO;
    //우승이력(대회)
    private ConferenceListDTO conferenceListDTO;
}
