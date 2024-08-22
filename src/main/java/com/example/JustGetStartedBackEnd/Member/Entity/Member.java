package com.example.JustGetStartedBackEnd.Member.Entity;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.Member.DTO.MemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "password")
    private String password;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role")
    private MemberRole role;

    @NotBlank
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "introduce")
    private String introduce;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<Community> communities = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY)
    private List<Conference> conferences = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "referee", fetch = FetchType.LAZY)
    private List<GameMatch> gameMatches = new ArrayList<>();

    @OneToMany(mappedBy = "pubMember", fetch = FetchType.LAZY)
    private List<JoinNotification> joinNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "writter", fetch = FetchType.LAZY)
    private List<TeamReview> teamReviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TeamInviteNotification> teamInviteNotifications = new ArrayList<>();

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void update(String name, String profileImage, String introduce) {
        this.name = name;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    public MemberDTO toMemberDTO(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(this.memberId);
        memberDTO.setName(this.name);
        memberDTO.setProfileImage(this.profileImage);
        memberDTO.setEmail(this.email);
        memberDTO.setRole(this.getRoleKey());
        memberDTO.setIntroduce(this.introduce);
        return memberDTO;
    }

    @Builder
    public Member(String password, String email, String name, MemberRole role, String profileImage, String profileName, String introduce) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.introduce = introduce;
    }
}
