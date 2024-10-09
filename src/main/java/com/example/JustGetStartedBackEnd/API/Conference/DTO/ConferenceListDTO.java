package com.example.JustGetStartedBackEnd.API.Conference.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConferenceListDTO {
    private List<ConferenceDTO> conferenceDTOList;
}
