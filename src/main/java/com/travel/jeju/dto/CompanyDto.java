package com.travel.jeju.dto;

import lombok.Data;

@Data
public class CompanyDto {


	private Integer c_pk_num; // 사업장 일련번호

	private String c_cnum; // 사업자번호 [ Business Number ]
	
	private String c_fk_id; // 사업자 유저 아이디

	private String c_name; // 업체명

	private String c_phone; // 업체 전화번호

	private String c_category; // 업체 카테고리 [ 숙박, 레저, 식당 ]

	private String c_addr; // 업체 주소

	private String c_contents; // 업체 소개글

	private String c_condition; // 영업 여부

	private String c_check; // 업체 승인여부

	private String c_img; // 엽체 대문 사진

	private String c_type; // 업체 주 종목 { 숙박 : [ 호텔, 모텔 ] ,  레저 : [ 수상스키, 바이크 ], 식당 : [ 일식, 중식 ]}

	private Double c_lat; // 업체 좌표 위도 [ ex => 36.312 ]

	private Double c_lon; // 업체 좌표 경도 [ ex => 127.312 ]

	private Integer c_file_group_no; // 업체 파일 그룹데이터

	
	
	private String fa_pk_num;
	private String fa_fk_id;
	private String fa_fk_cnum;
}
