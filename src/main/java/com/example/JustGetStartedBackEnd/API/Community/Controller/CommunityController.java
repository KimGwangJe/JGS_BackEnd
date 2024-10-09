package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;
    private final int SIZE = 20;

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityInfoDTO> getCommunityInfo(@PathVariable("communityId") Long communityId) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findById(communityId));
    }

    @GetMapping
    public ResponseEntity<PagingResponseDTO<CommunityDTO>> getCommunityList(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(required = false) String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAll(page, SIZE, keyword));
    }
}
