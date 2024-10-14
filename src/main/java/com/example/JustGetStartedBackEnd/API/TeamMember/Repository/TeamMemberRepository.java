package com.example.JustGetStartedBackEnd.API.TeamMember.Repository;

import com.example.JustGetStartedBackEnd.API.TeamMember.Entity.TeamMember;
import com.example.JustGetStartedBackEnd.API.TeamMember.Repository.QueryDSL.TeamMemberQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberQueryDSL {
}
