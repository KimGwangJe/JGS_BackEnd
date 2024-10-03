package com.example.JustGetStartedBackEnd.API.Conference.Controller;

import com.example.JustGetStartedBackEnd.API.Conference.DTO.ConferencePagingDTO;
import com.example.JustGetStartedBackEnd.API.Conference.Service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conference")
@RequiredArgsConstructor
public class ConferenceController {
    private final ConferenceService conferenceService;
    private final int SIZE = 20;

    @GetMapping
    public ResponseEntity<ConferencePagingDTO> getConferenceList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword){
        ConferencePagingDTO conferencePagingDTO = conferenceService.getConferenceList(page, SIZE, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(conferencePagingDTO);
    }
}
