package com.example.JustGetStartedBackEnd.API.Match.Controller;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchInfoDTO;
import com.example.JustGetStartedBackEnd.API.Match.Service.MatchService;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final int SIZE = 20;

    @GetMapping
    public ResponseEntity<PagingResponseDTO<MatchInfoDTO>> getAllMatch(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(required = false) String tier){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findAll(page, SIZE, keyword, tier));
    }
}
