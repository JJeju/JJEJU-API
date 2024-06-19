package com.travel.jeju.model.travel.plan;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePlanModel {

    @NotNull
    private Integer tr_pk_num; // 계획요소 일련번호

    private LocalDateTime plan_start_date_time; // 계획 요소 시작 날짜와 시간

    private LocalDateTime plan_end_date_time; // 계획 요소 끝 날짜와 시간

    private String rm; // 계획요소 메모

}
