package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    @Transactional(readOnly = true)
    public PagingResponseDTO<ConferenceDTO> getConferenceList(int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page, size);

        Page<Conference> conferencePage = (keyword == null || keyword.isEmpty())
                ? conferenceRepository.findAll(pageable)
                : conferenceRepository.findByConferenceNameKeyword(keyword, pageable);

        List<ConferenceDTO> conferenceDTOList = conferencePage.getContent().stream()
                .map(Conference::toConferenceDTO)
                .collect(Collectors.toList());

        return new PagingResponseDTO<>(conferencePage, conferenceDTOList);
    }
}
