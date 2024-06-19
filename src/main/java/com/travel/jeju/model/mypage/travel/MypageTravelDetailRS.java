package com.travel.jeju.model.mypage.travel;

import java.util.List;

import com.travel.jeju.dto.EventDto2;
import com.travel.jeju.dto.TravelrouteDto;

import lombok.Data;

@Data
public class MypageTravelDetailRS {

    private int price; 

    private List<EventDto2> event;

    private TravelrouteDto travel;
}
