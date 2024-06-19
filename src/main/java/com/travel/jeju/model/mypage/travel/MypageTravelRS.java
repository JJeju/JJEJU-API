package com.travel.jeju.model.mypage.travel;

import java.util.List;

import com.travel.jeju.dto.TravelrouteDto;

import lombok.Data;

@Data
public class MypageTravelRS {

    private List<TravelrouteDto> travel;
    
}
