package com.example.JustGetStartedBackEnd.API.TeamInvite.Repository;

import com.example.JustGetStartedBackEnd.API.TeamInvite.Entity.TeamInviteNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInviteRepository extends JpaRepository<TeamInviteNotification, Long> {
}
