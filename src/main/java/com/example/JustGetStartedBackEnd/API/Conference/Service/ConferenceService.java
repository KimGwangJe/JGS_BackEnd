package com.example.JustGetStartedBackEnd.API.Conference.Service;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferenceDTO;
import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferencePagingDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Entity.Conference;
import com.example.JustGetStartedBackEnd.API.Conference.Repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    public ConferencePagingDTO getConferenceList(int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page, size);

        Page<Conference> conferencePage = (keyword == null || keyword.isEmpty())
                ? conferenceRepository.findAll(pageable)
                : conferenceRepository.findByConferenceNameKeyword(keyword, pageable);

        List<ConferenceDTO> conferenceDTOList = conferencePage.getContent().stream()
                .map(Conference::toConferenceDTO)
                .collect(Collectors.toList());

        ConferencePagingDTO pagingDTO = new ConferencePagingDTO();
        pagingDTO.setConferenceDTOList(conferenceDTOList);
        pagingDTO.setPageNo(conferencePage.getNumber());
        pagingDTO.setPageSize(conferencePage.getSize());
        pagingDTO.setTotalElements(conferencePage.getTotalElements());
        pagingDTO.setTotalPages(conferencePage.getTotalPages());
        pagingDTO.setLast(conferencePage.isLast());

        return pagingDTO;
    }
}
