package com.example.JustGetStartedBackEnd.Domain;

import com.example.JustGetStartedBackEnd.Member.Member;
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

    @Builder
    public Conference(String conferenceName, Member organizer, Date conferenceDate, String content, Team winnerTeam) {
        this.conferenceName = conferenceName;
        this.organizer = organizer;
        this.conferenceDate = conferenceDate;
        this.content = content;
        this.winnerTeam = winnerTeam;
    }
}
