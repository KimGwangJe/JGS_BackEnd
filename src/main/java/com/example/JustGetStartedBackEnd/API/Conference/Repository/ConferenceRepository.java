package com.example.JustGetStartedBackEnd.API.Conference.Repository;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL.ConferenceQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, String>, ConferenceQueryDSL {
}
