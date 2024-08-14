package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamJoinNotificationRepository extends JpaRepository<JoinNotification, Long> {
    @Query("SELECT jn FROM JoinNotification jn WHERE jn.community.communityId = :communityId And jn.pubMember.memberId = :memberId")
    JoinNotification findByMemberIdAndCommunityId(@Param("memberId") Long memberId, @Param("communityId") Long communityId);

    @Query("SELECT jn FROM JoinNotification jn WHERE jn.community.writer.memberId = :memberId")
    List<JoinNotification> findByWriterMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE JoinNotification jn SET jn.isRead = true WHERE jn.community.writer.memberId = :memberId")
    void updateReadStatusByMemberId(@Param("memberId") Long memberId);
}
