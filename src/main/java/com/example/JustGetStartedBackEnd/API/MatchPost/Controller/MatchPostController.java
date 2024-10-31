package com.example.JustGetStartedBackEnd.API.MatchPost.Controller;

import com.example.JustGetStartedBackEnd.API.MatchPost.DTO.MatchPostDTO;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.MatchPostService;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match-post")
public class MatchPostController {
    private final MatchPostService matchPostService;
    private final int SIZE = 20;

    @GetMapping
    public ResponseEntity<PagingResponseDTO<MatchPostDTO>> getMatchPostList(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(required = false) String keyword,
                                                                            @RequestParam(required = false) String tier){
        return ResponseEntity.status(HttpStatus.OK).body(matchPostService.getMatchPostList(page, SIZE, keyword, tier));
    }
}
