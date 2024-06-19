package com.travel.jeju.dto;

import lombok.Data;

@Data
public class CimgDto {
	private Integer ci_pk_num; // 업체 대문 사진 일련번호 
	private Integer ci_fk_num; // 업체 일련번호
	private String ci_oriname; // 사진 원본 이름
	private String ci_sysname; // 사진 저장용 변환한 이름
}
