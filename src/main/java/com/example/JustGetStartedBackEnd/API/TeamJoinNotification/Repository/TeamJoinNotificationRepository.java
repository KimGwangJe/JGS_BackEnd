package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJoinNotificationRepository extends JpaRepository<JoinNotification, Long> {
    @Query("SELECT jn FROM JoinNotification jn WHERE jn.community.communityId = :communityId And jn.pubMember.memberId = :memberId")
    JoinNotification findByMemberIdAndCommunityId(@Param("memberId") Long memberId, @Param("communityId") Long communityId);
}
