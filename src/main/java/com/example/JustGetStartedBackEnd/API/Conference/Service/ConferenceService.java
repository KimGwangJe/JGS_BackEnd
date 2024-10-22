package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    @Transactional(readOnly = true)
    public PagingResponseDTO<ConferenceDTO> getConferenceList(int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page, size);

        Page<ConferenceDTO> conferencePage = conferenceRepository.searchPagedConferences(keyword, pageable);

        List<ConferenceDTO> conferenceDTOList = conferencePage.getContent().stream().toList();

        return PagingResponseDTO.of(conferencePage, conferenceDTOList);
    }
}
