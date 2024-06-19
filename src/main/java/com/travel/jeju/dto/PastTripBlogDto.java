package com.travel.jeju.dto;

import lombok.Data;

@Data
public class PastTripBlogDto {
	private int b_pk_num; // 후기 일련번호 
	private int b_fk_tnum; // 후기 남기는 여행 일련번호
	private String b_fk_id; // 후기 작성자 아이디
	private String b_img; // 후기 이미지
	private String tr_title; // 여행 제목
	private String tr_in; // 여행 시작일
	private String tr_out; // 여행 끝일
	private boolean tr_tf; // 여행 완료여부
}
