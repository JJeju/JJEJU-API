package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventDto2 {
	
	private String title; // 이벤트 제목

	private LocalDateTime start; // 이벤트 시작일시

	private LocalDateTime end; // 이벤트 종료일시

	private Integer cnum; // 이벤트 영업장(?)
	
	private Integer e_num; // 이벤트 일련번호

	private Boolean e_pay; // 금액?

	private String e_id; // 이벤트 개최 사용자(?)

	private String e_relationship; // (?)
}
