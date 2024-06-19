package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.SmsDto;
import com.travel.jeju.dto.TokenBlackDto;

@Mapper
public interface MemberMapper {
	// Member테이블 insert
	public void memberInsert(MemberDto dto);

	// authorities 테이블 insert
	public void memberAuthoritiesInsert(MemberDto dto);
	
	// m_username로 비밀번호 구하기
	public String pwdSelect(String m_username);
	// m_username로 dto 구하기
	public MemberDto memberSelect(String m_username);

	public List<String> memberAuthoritiesSelect(String m_username);

	public List<MemberDto> memberAllSelect();
	// m_username가 있으면 1을 반환
	public int idCheck(String m_username);
	// m_nickname이 있으면 1을반환
	public int nickCheck(String m_nickname);
	// m_username 로 member테이블 delete
	public void memberDelete(String m_username);

	public void upgradeMember(String id);
	
	
	// 개인정보 수정
	public void memberUpdate(MemberDto mem);
	//비밀번호 수정
	public void pwUpdate(MemberDto mem);
	
	public int PwdCheck(String m_pass);

	public void memberBlack(String id);

	// 로그인 성공시 업데이트
	public void loginSuccessUpdate(MemberDto mem);

	// 로그인 실패시 업데이트
	public void loginFailUpdate(MemberDto mem);

	public boolean insertSms(SmsDto dto);

	public int certValid(String phone_num);

	public SmsDto selectCertMessageBas(String phone_num);

    public boolean updateSms(int idx);

    public void updateLogout(String username);

    public void insertTokenBlack(TokenBlackDto tokenBlackModel);

	public int refreshTokenValid(TokenBlackDto dto);

	public void deleteTestId(String username);

	public void deleteTestAuth(String username);

	public void deleteTestSms(String phone_num);

}
