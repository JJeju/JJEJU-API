package com.travel.jeju.model.member.profile;

import lombok.Data;

@Data
public class ProfileModel {

    private String username; // 아이디 PK

    private String name; // 회원 이름

	private String gender; // 성별

	private String nickname; // 닉네임

	private String birth; // 생년월일

	private String phone; // 휴대폰 번호

	private String addr; // 주소

	private String email; // 이메일

	private String category; // 회원 분류 [ 일반유저, 업체유저, 관리자유저 ]
    
}
