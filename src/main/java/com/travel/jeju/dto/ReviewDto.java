package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewDto {

	private Integer rv_pk_num; // 리뷰 일련번호

	private Integer rv_fk_cnum; // 리뷰가 쓰여진 업체 일련번호

	private String rv_fk_id; // 리뷰 작성자 아이디

	private String rv_img; // 리뷰 사진

	private LocalDateTime rv_date; // 리뷰 작성일시

	private String rv_contents; // 리뷰 내용

	private Integer rv_star; // 별점

	private String rv_answer; // 답변 내용

	private LocalDateTime rv_updatedate; // 수정일시 [ 답변 달리면 쓰여질 듯 ? ]

	private String rv_check; // 답변 여부

	private Integer file_group_no; // 리뷰 관련 파일 그룹번호
	
	private String filecheck; // 파일 체크
}
