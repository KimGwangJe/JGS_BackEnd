package com.example.JustGetStartedBackEnd.API.Notification.Repository;

import com.example.JustGetStartedBackEnd.API.Notification.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.member.memberId = :memberId")
    void deleteByMemberId(Long memberId);

    @Query("SELECT n FROM Notification n WHERE n.member.memberId = :memberId")
    List<Notification> findByMemberId(Long memberId);
}
