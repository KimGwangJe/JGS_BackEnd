package com.example.JustGetStartedBackEnd.API.Community.Entity;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime recruitDate;

    @Column(name = "write_date")
    private Date writeDate;

    @JoinColumn(name = "writer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @JoinColumn(name = "team_name")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<JoinNotification> joinNotifications = new ArrayList<>();

    public void updateContentAndTitle(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public CommunityDTO getCommunityPaging(){
        CommunityDTO communityDTO = new CommunityDTO();
        communityDTO.setTitle(this.title);
        communityDTO.setCommunityId(this.communityId);
        communityDTO.setRecruit(this.recruit);
        if(this.recruit){
            communityDTO.setTeamName(this.team.getTeamName());
            communityDTO.setRecruitDate(this.recruitDate);
        }
        return communityDTO;
    }

    public CommunityInfoDTO getCommunityInfo(){
        CommunityInfoDTO communityInfoDTO = new CommunityInfoDTO();
        communityInfoDTO.setTitle(this.title);
        communityInfoDTO.setCommunityId(this.communityId);
        communityInfoDTO.setRecruit(this.recruit);
        communityInfoDTO.setContent(this.content);
        communityInfoDTO.setMemberId(this.writer.getMemberId());
        communityInfoDTO.setWriteDate(this.writeDate);

        //모집 글일 경우에만 팀이름과 모집 기간 데이터 있음
        if(this.recruit){
            communityInfoDTO.setTeamName(this.team.getTeamName());
            communityInfoDTO.setRecruitDate(this.recruitDate);
        }

        return communityInfoDTO;
    }

    @Builder
    Community(String title, String content, boolean recruit, LocalDateTime recruitDate, Date writeDate, Member writer, Team team) {
        this.title = title;
        this.content = content;
        this.recruit = recruit;
        this.recruitDate = recruitDate;
        this.writeDate = writeDate;
        this.writer = writer;
        this.team = team;
    }
}
