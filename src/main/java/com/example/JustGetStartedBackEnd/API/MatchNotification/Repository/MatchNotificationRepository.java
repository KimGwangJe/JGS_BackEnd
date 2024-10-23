package com.example.JustGetStartedBackEnd.API.MatchNotification.Repository;

import com.example.JustGetStartedBackEnd.API.MatchNotification.Entity.MatchNotification;
import com.example.JustGetStartedBackEnd.API.MatchNotification.Repository.QueryDSL.MatchNotificationQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchNotificationRepository extends JpaRepository<MatchNotification, Long>, MatchNotificationQueryDSL {
}