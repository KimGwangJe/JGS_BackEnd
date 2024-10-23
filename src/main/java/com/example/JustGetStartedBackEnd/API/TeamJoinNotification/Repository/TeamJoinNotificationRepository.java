package com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository;

import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Entity.JoinNotification;
import com.example.JustGetStartedBackEnd.API.TeamJoinNotification.Repository.QueryDSL.TeamJoinNotificationQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJoinNotificationRepository extends JpaRepository<JoinNotification, Long>, TeamJoinNotificationQueryDSL {
}
