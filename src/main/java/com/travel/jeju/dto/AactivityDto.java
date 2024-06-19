package com.travel.jeju.dto;

import lombok.Data;

@Data
public class AactivityDto {

	private Integer at_pk_num; // 레저 상품 일련번호

	private Integer at_fk_cnum; // 레저 영업장 일련번호

	private String at_name; // 레저 상품명

	private String at_img; // 레저 대문 상품이미지

	private String at_contents; // 레저 상품 소개글

	private Integer at_price; // 레저 상품 금액

	private String at_in; // 레저 예약가능 시간

	private String at_out; // 레저 예약마감 시간

	private String at_rtch; // 레저 예약가능 여부

	private String at_hidden; // 레저 상품 숨김여부

	private String filecheck; // 파일 체크여부

}
