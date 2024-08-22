package com.example.JustGetStartedBackEnd.API.Notification.Repository;

import com.example.JustGetStartedBackEnd.API.Notification.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
