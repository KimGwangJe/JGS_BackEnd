package com.example.JustGetStartedBackEnd.API.CommonNotification.Repository;

import com.example.JustGetStartedBackEnd.API.CommonNotification.Entity.Notification;
import com.example.JustGetStartedBackEnd.API.CommonNotification.Repository.QueryDSL.NotificationQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationQueryDSL {
}
