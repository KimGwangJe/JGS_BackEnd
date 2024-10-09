package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
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
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;
    private final int SIZE = 20;

    @GetMapping
    public ResponseEntity<CommunityInfoDTO> getCommunityInfo(@RequestParam("communityId") Long communityId) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findById(communityId));
    }

    @GetMapping("/all")
    public ResponseEntity<PagingResponseDTO<CommunityDTO>> getCommunityList(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(required = false) String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAll(page, SIZE, keyword));
    }
}
