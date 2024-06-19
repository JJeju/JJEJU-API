package com.travel.jeju.model.travel.plan;

import java.util.List;

import com.travel.jeju.dto.TravelPlanDto;
import com.travel.jeju.dto.TravelrouteDto;

import lombok.Data;

@Data
public class TravelPlanSelectRS {

    private DaysModel[] planList;

    private List<TravelPlanDto> planItemList;

    private TravelrouteDto travelroute;

}
