package com.travel.jeju.model.travel.plan;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TravelPlanUpdateRQ {

    @Valid
    @NotNull
    @Size(min = 1)
    private List<UpdatePlanModel> updatePlanList;

    @NotNull
    private Integer t_pk_num;

}
