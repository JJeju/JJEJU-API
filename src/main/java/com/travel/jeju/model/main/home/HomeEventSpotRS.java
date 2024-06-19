package com.travel.jeju.model.main.home;

import java.util.List;

import com.travel.jeju.dto.EventDto;
import com.travel.jeju.dto.SpotDto;

import lombok.Data;

@Data
public class HomeEventSpotRS {

    private List<EventDto> event;
    private List<SpotDto> spot;
}
