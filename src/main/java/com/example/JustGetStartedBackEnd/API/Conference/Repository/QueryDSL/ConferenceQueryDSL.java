package com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceQueryDSL {
    Page<Conference> findByConferenceNameKeyword(String keyword, Pageable pageable);
}
