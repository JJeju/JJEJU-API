package com.travel.jeju.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Alias("MemberDto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
	
	private String m_username; // 아이디 PK

	private String m_pass; // 암호

	private String m_name; // 회원 이름

	private String m_gender; // 성별

	private String m_nickname; // 닉네임

	private String m_birth; // 생년월일

	private String m_phone; // 휴대폰 번호

	private String m_addr; // 주소

	private String m_email; // 이메일

	private String m_category; // 회원 분류 [ 일반유저, 업체유저, 관리자유저 ]
	
	private boolean m_enabled;
	private boolean m_account_non_lock;
	private boolean m_account_non_expired;
	private int m_pass_fail_count;
	private String m_refresh_token;
}
