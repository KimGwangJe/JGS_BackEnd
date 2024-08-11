package com.example.JustGetStartedBackEnd.API.Team.Repository;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, String>{
    @Query("SELECT t FROM Team t " +
            "LEFT JOIN FETCH t.teamMembers tm " +
            "WHERE t.teamName = :teamName")
    Team findByTeamName(@Param("teamName") String teamName);

    @Query("SELECT t FROM Team t " +
            "WHERE LOWER(t.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Team> findByTeamNameKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Team t " +
            "WHERE t.tier.tierId = :tierId AND " +
            "LOWER(t.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Team> findByTierAndKeyword(@Param("tierId") Long tierId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Team t WHERE t.tier.tierId = :tierId")
    Page<Team> findByTier(@Param("tierId") Long tierId, Pageable pageable);

}
