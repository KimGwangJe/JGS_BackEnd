package com.example.JustGetStartedBackEnd.Domain;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
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
@Table(name="community")
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    @NotBlank
    @Column(name="title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "recruit")
    private boolean recruit;

    @Column(name = "recruit_date")
    private Date recruitDate;

    @Column(name = "write_date")
    private Date writeDate;

    @JoinColumn(name = "writer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @JoinColumn(name = "team_name")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private List<JoinNotification> joinNotifications = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

    public CommunityDTO getCommuntiyPaging(){
        CommunityDTO communityDTO = new CommunityDTO();
        communityDTO.setTitle(this.title);
        communityDTO.setCommunityId(this.communityId);
        communityDTO.setRecruit(this.recruit);
        communityDTO.setTeamName(this.team.getTeamName());
        communityDTO.setRecruitDate(this.recruitDate);

        return communityDTO;
    }

    @Builder
    Community(String title, String content, boolean recruit, Date recruitDate, Date writeDate, Member writer, Team team) {
        this.title = title;
        this.content = content;
        this.recruit = recruit;
        this.recruitDate = recruitDate;
        this.writeDate = writeDate;
        this.writer = writer;
        this.team = team;
    }
}
