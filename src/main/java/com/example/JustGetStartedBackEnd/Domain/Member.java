package com.example.JustGetStartedBackEnd.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private int memberId;

    @Column(name = "password")
    private String password;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "role")
    private String role;

    @NotBlank
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "profile_name")
    private String profileName;

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

    @Builder
    public Member(String password, String email, String name, String role, String profileImage, String profileName) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.profileImage = profileImage;
        this.profileName = profileName;
    }
}
