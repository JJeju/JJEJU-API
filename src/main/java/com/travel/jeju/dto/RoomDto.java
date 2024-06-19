package com.travel.jeju.dto;

import lombok.Data;

@Data
public class RoomDto {

	private Integer r_pk_num; // 숙박 방 아이템 일련번호

	private Integer r_fk_cnum; // 영업장 일련번호

	private String r_name; // 방 이름

	private String r_img; // 방 사진

	private String r_contents; // 방 소개글 

	private Integer r_price; // 방 금액

	private String r_in; // 입실 시간

	private String r_out; // 퇴실 시간

	private String r_hidden; // 숨김여부

	private String filecheck; // 파일 체크여부 , 당시에는 이게 1이면 파일이 있다고 가정하여 파일 타는 로직을 태웠음, 지금은 없어도 될지도 ㅋㅋ
}
