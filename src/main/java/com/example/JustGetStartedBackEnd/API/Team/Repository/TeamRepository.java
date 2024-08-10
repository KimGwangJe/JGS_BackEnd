package com.example.JustGetStartedBackEnd.API.Team.Repository;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, String> {
    @Query("SELECT t FROM Team t " +
            "LEFT JOIN FETCH t.teamMembers tm " +
            "WHERE t.teamName = :teamName")
    Team findByTeamName(@Param("teamName") String teamName);
}
