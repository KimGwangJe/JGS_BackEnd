package com.example.JustGetStartedBackEnd.API.Conference.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceQueryDSL {
    Page<ConferenceDTO> searchPagedConferences(String keyword, Pageable pageable);
}
