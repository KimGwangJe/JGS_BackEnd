package com.example.JustGetStartedBackEnd.API.Conference.Entity;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "conference")
@NoArgsConstructor
public class Conference {

    @Id
    @Column(name = "conference_name")
    private String conferenceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer")
    private Member organizer;

    @Column(name = "conference_date")
    private Date conferenceDate;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team")
    private Team winnerTeam;

    public void updateWinnerTeam(Team winnerTeam){
        this.winnerTeam = winnerTeam;
    }

    public void udpateConferenceInfo(ConferenceInfoDTO conferenceInfoDTO){
        this.conferenceName = conferenceInfoDTO.getConferenceName();
        this.content = conferenceInfoDTO.getContent();
        this.conferenceDate = conferenceInfoDTO.getConferenceDate();
    }

    @Builder
    public Conference(String conferenceName, Member organizer, Date conferenceDate, String content, Team winnerTeam) {
        this.conferenceName = conferenceName;
        this.organizer = organizer;
        this.conferenceDate = conferenceDate;
        this.content = content;
        this.winnerTeam = winnerTeam;
    }

    public ConferenceDTO toConferenceDTO() {
        ConferenceDTO dto = new ConferenceDTO();
        dto.setConferenceName(this.conferenceName);
        dto.setWinnerTeam(this.winnerTeam.getTeamName());
        dto.setConferenceDate(this.conferenceDate);
        dto.setOrganizer(this.organizer.getMemberId());
        dto.setContent(this.content);
        return dto;
    }
}
