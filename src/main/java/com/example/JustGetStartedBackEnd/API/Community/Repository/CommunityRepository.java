package com.example.JustGetStartedBackEnd.API.Community.Repository;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT c FROM Community c WHERE LOWER(c.title) = LOWER(:keyword) OR LOWER(c.team.teamName) = LOWER(:keyword)")
    Page<Community> findByTeamNameAndTitle(@Param("keyword") String keyword, Pageable pageable);
}
