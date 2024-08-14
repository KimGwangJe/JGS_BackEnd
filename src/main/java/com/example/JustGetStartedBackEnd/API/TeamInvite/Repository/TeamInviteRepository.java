package com.example.JustGetStartedBackEnd.API.TeamInvite.Repository;

import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamInviteRepository extends JpaRepository<TeamInviteNotification, Long> {

    @Query("SELECT ti FROM TeamInviteNotification ti WHERE ti.member.memberId = :memberId")
    List<TeamInviteNotification> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE TeamInviteNotification t SET t.isRead = true WHERE t.member.memberId = :memberId")
    void updateReadStatusByMemberId(@Param("memberId") Long memberId);

}
