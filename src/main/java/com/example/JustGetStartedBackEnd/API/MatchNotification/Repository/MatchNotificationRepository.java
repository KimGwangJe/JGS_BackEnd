package com.example.JustGetStartedBackEnd.API.MatchNotification.Repository;

import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchNotificationRepository extends JpaRepository<MatchNotification, Long> {

    @Query("SELECT mn FROM MatchNotification mn WHERE mn.appliTeamName.teamName = :teamName And mn.matchPostId.matchPostId = :matchPostId")
    MatchNotification findByMatchPostIdAndTeamName(@Param("matchPostId") Long matchPostId, @Param("teamName") String teamName);

    @Modifying
    @Query("DELETE FROM MatchNotification mn WHERE mn.matchPostId.matchPostId = :matchPostId")
    void deleteAllByMatchPostId(@Param("matchPostId") Long matchPostId);
}
