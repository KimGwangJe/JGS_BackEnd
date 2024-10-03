package com.example.JustGetStartedBackEnd.API.Match.Repository;

import com.example.JustGetStartedBackEnd.API.Match.Entity.GameMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {

    @Query("SELECT gt FROM GameMatch gt " +
            "WHERE LOWER(gt.teamA.teamName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(gt.teamB.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<GameMatch> findByTeamNameKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT gt FROM GameMatch gt " +
            "WHERE (gt.teamA.tier.tierId = :tierId OR gt.teamB.tier.tierId = :tierId) " +
            "AND (LOWER(gt.teamA.teamName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(gt.teamB.teamName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<GameMatch> findByTierAndKeyword(@Param("tierId") Long tierId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT gt FROM GameMatch gt WHERE gt.teamA.tier.tierId = :tierId OR gt.teamB.tier.tierId = :tierId")
    Page<GameMatch> findByTier(@Param("tierId") Long tierId, Pageable pageable);
}
