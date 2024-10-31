package com.example.JustGetStartedBackEnd.API.Team.Controller;

import com.example.JustGetStartedBackEnd.API.Common.DTO.PagingResponseDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.TeamDTO;
import com.example.JustGetStartedBackEnd.API.Team.DTO.Response.TeamInfoDTO;
import com.example.JustGetStartedBackEnd.API.Team.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@Validated
public class TeamController {
        private final TeamService teamService;
        private final int SIZE = 10;

        //페이징 처리
        @GetMapping
        public ResponseEntity<PagingResponseDTO<TeamDTO>> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(required = false) String keyword,
                                                                      @RequestParam(required = false) String tier
                                                       ) {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findAll(page, SIZE, keyword, tier));
        }

        @GetMapping("/info")
        public ResponseEntity<TeamInfoDTO> getTeamInfo(
                @NotBlank(message = "팀명은 Null일 수 없습니다.") @RequestParam("teamName") String teamName) {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findByTeamName(teamName));
        }
}