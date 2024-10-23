package com.example.JustGetStartedBackEnd.API.Community.Controller;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityDTO;
import com.example.JustGetStartedBackEnd.API.Community.DTO.Response.CommunityInfoDTO;
import com.example.JustGetStartedBackEnd.API.Community.Service.CommunityService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
@Validated
public class CommunityController {
    private final CommunityService communityService;
    private final int SIZE = 20;

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityInfoDTO> getCommunityInfo(
            @NotNull @Min(value=1, message="Community ID는 1 이상이어야 됩니다.")
            @PathVariable("communityId") Long communityId) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findById(communityId));
    }

    @GetMapping
    public ResponseEntity<PagingResponseDTO<CommunityDTO>> getCommunityList(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(required = false) String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAll(page, SIZE, keyword));
    }
}
