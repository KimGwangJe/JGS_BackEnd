package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityListPageDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;
    private final int SIZE = 20;

    @GetMapping("/all")
    public ResponseEntity<CommunityListPageDTO> getCommunityList(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(required = false) String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAll(page, SIZE, keyword));
    }
}
