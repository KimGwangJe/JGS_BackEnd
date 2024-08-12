package com.example.JustGetStartedBackEnd.API.MatchPost.Repository;

import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchPostRepository extends JpaRepository<MatchPost, Long> {

    @Query("SELECT mp FROM MatchPost mp WHERE mp.teamA.tier.tierId = :tierId")
    Page<MatchPost> findByTier(@Param("tierId")Long tierId, Pageable pageable);

    @Query("SELECT mp FROM MatchPost mp " +
            "WHERE LOWER(mp.teamA.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MatchPost> findByTeamNameKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT mp FROM MatchPost mp " +
            "WHERE mp.teamA.tier.tierId = :tierId AND " +
            "LOWER(mp.teamA.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MatchPost> findByTierAndKeyword(@Param("tierId") Long tierId, @Param("keyword") String keyword, Pageable pageable);
}
