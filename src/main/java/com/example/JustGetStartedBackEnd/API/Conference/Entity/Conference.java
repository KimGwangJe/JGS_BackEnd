package com.example.JustGetStartedBackEnd.API.Conference.Entity;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.Request.ConferenceInfoDTO;
import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate conferenceDate;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team")
    private Team winnerTeam;

    public void updateWinnerTeam(Team winnerTeam){
        this.winnerTeam = winnerTeam;
    }

    public void updateConferenceInfo(ConferenceInfoDTO conferenceInfoDTO){
        this.conferenceName = conferenceInfoDTO.conferenceName();
        this.content = conferenceInfoDTO.content();
        this.conferenceDate = conferenceInfoDTO.conferenceDate();
    }

    @Builder
    public Conference(String conferenceName, Member organizer, LocalDate conferenceDate, String content, Team winnerTeam) {
        this.conferenceName = conferenceName;
        this.organizer = organizer;
        this.conferenceDate = conferenceDate;
        this.content = content;
        this.winnerTeam = winnerTeam;
    }

    public ConferenceDTO toConferenceDTO() {
        ConferenceDTO dto = new ConferenceDTO();
        dto.setConferenceName(this.conferenceName);
        if(this.winnerTeam != null){
            dto.setWinnerTeam(this.winnerTeam.getTeamName());
        }
        dto.setConferenceDate(this.conferenceDate);
        dto.setOrganizer(this.organizer.getMemberId());
        dto.setContent(this.content);
        return dto;
    }
}
