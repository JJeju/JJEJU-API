package com.travel.jeju.dto;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("travelPlanDto")
public class TravelPlanDto {

    private Integer tp_pk_num; // 여행계획요소 일련번호

    private Integer tp_fk_tnum; // 여행 참조번호

    // private Integer tp_fk_item_num; // 아이템[상품] 참조변호

    private Integer tp_fk_company_num; // 영업장 참조변호

    private String tp_item_category; // 상품카테고리 [ ROOM : 숙박, LEISURE : 레저, RESTAURANT : 식당]
    
    private LocalDateTime tp_plan_start_date_time; // 계획 요소 시작 날짜와 시간

    private LocalDateTime tp_plan_end_date_time; // 계획 요소 끝 날짜와 시간

    private String tp_rm; // 메모 [Null값이여도 됨]

    private LocalDateTime create_dt; // 생성일시 

    private LocalDateTime update_dt; // 생성일시 

}
