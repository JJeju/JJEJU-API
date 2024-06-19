package com.travel.jeju.dto;

import java.time.LocalDateTime;

import com.travel.jeju.model.common.FileModel;

import lombok.Data;

@Data
public class BlogDto {
	
	private Integer b_pk_num; //후기번호

	private Integer b_fk_tnum; //여행번호

	private String b_fk_id; //회원아이디

	private FileModel b_img; //대표이미지

	private LocalDateTime b_date; //작성날짜

	private String b_title; //제목

	private Integer b_cost; //총경비

	private LocalDateTime b_create_dt; // 생성일시
	
	private String b_contents; //내용
	
	private Integer file_group_no; // 파일그룹번호
	
	private Boolean b_public_check; // 공개여부

	private Integer b_star; // 별점 [ 여행 만족도 ]
	
}
