package com.example.JustGetStartedBackEnd.API.Match.DTO.Response;

import com.example.JustGetStartedBackEnd.API.Match.DTO.MatchDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchListDTO {
    private List<MatchDTO> matches;
}
