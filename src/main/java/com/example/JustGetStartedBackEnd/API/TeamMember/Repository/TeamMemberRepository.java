package com.example.JustGetStartedBackEnd.API.TeamMember.Repository;

import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT tm FROM TeamMember tm WHERE tm.member.memberId = :memberId")
    List<TeamMember> findAllByMemberId(Long memberId);
}
