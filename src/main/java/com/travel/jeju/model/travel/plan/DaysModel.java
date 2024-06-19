package com.travel.jeju.model.travel.plan;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class DaysModel {

    private List<SelectPlanModel> dayPlanList;

    private LocalDate day;

}
