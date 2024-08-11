package com.example.JustGetStartedBackEnd.API.TeamMember.Entity;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.TeamMember.DTO.TeamMemberDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="team_member")
@NoArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_name")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private TeamMemberRole role;

    @Builder
    TeamMember(Member member, Team team, TeamMemberRole role) {
        this.member = member;
        this.team = team;
        this.role = role;
    }

    public TeamMemberDTO toTeamMemberDTO() {
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setTeamName(this.team.getTeamName());
        dto.setTeamMemberId(this.teamMemberId);
        dto.setTeamMemberName(this.member.getName());
        dto.setMemberId(this.member.getMemberId());
        dto.setProfileImage(this.member.getProfileImage());
        dto.setRole(this.role);
        return dto;
    }
}
