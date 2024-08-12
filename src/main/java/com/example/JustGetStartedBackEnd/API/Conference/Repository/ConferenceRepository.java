package com.example.JustGetStartedBackEnd.API.Conference.Repository;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConferenceRepository extends JpaRepository<Conference, String> {

    @Query("SELECT c FROM Conference c WHERE c.conferenceName = :keyword")
    Page<Conference> findByConferenceNameKeyword(@Param("keyword") String keyword, Pageable pageable);
}
