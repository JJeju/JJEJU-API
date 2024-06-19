package com.travel.jeju.dto;

import lombok.Data;

@Data
public class RevDto {

	private Integer rv_pk_num; // 리뷰 

	private Integer rv_fk_cnum; // 리뷰 작성한 업체 일련번호

	private String rv_fk_id; // 리뷰 작성한 유저 아이디

	private String rv_img; // 리뷰 이미지

	private String rv_date; // 리뷰 작성일시 

	private String rv_contents; // 리뷰 내용

	private Integer rv_star; // 별점

	private String c_cnum; // 업체 사업자 번호

	private String c_name; // 업체 이름

	private String c_category; // 업체종류 [ 숙박, 레저, 식당 ]
}
