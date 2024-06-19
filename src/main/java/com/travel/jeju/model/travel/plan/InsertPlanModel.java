package com.travel.jeju.model.travel.plan;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertPlanModel {

    @NotEmpty
    private String category; // ROOM[숙박], LEISURE[레저], RESTAURANT[식당] 인지 구분

    @NotNull
    private Integer company_pk_num; // 영업장 번호

    @NotNull
    private LocalDateTime plan_start_date_time; // 계획 요소 시작 날짜와 시간

    @NotNull
    private LocalDateTime plan_end_date_time; // 계획 요소 끝 날짜와 시간

    private String rm; // 메모할 요소

}
