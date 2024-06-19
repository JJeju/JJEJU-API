package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleDto {

    private Integer idx; // 업체 일정 테이블 일련번호

    private Integer sc_fk_product_num; // 상품번호

    private Integer sc_fk_order_num; // 주문번호

    private LocalDateTime sc_start_date_time; // 예약 시작 날짜 및 시간

    private LocalDateTime sc_end_date_time; // 예약 종료 날짜 및 시간

    private LocalDateTime create_dt; // 생성일시

}
