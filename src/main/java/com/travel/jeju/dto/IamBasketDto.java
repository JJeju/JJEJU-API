package com.travel.jeju.dto;

import lombok.Data;

@Data
public class IamBasketDto {

	private Integer bk_fk_cnum; // 영업장 일련번호

	private Integer bk_fk_num; // 상품 일련번호

	private String bk_goods; // 상품명

	private Integer bk_price; // 상품 가격

	private String bk_name; // 주문자 명

	private boolean bk_paych; // 결제 여부

	private String bk_paytime; // 결제 일시

	private String bk_fk_id; // 주문자 ID
	
	private String c_category; // 업체 카테고리
	
	private String c_name; // 업체 명
	
	private String colname; // bk_paytime 또는 c_name  저장
	private String keyword; //검색단어.
	private Integer pageNum;
	private Integer listCnt;
}
