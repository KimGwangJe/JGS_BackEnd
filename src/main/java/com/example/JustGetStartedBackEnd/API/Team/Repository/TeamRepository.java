package com.example.JustGetStartedBackEnd.API.Team.Repository;

import com.example.JustGetStartedBackEnd.API.Team.Entity.Team;
import com.example.JustGetStartedBackEnd.API.Team.Repository.QeuryDSL.TeamQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String>, TeamQueryDSL {}
