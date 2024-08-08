package com.example.JustGetStartedBackEnd.Domain;

import com.example.JustGetStartedBackEnd.Member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="team_review")
@NoArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private int teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_name")
    private Team team;

    @NotBlank
    @Column(name="role")
    private String role;

    @Builder
    TeamMember(Member member, Team team, String role) {
        this.member = member;
        this.team = team;
        this.role = role;
    }
}
