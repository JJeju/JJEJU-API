package com.travel.jeju.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.constant.AuthConstants;
import com.travel.jeju.constant.UserType;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.SmsDto;
import com.travel.jeju.dto.TokenBlackDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.jwt.UserDetailsSecurityService;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.member.TokenModel;
import com.travel.jeju.model.member.cert_sms.CertSmsRQ;
import com.travel.jeju.model.member.insert_member.InsertMemeberRQ;
import com.travel.jeju.model.member.profile.ProfileModel;
import com.travel.jeju.model.member.profile.SelectProfileRS;
import com.travel.jeju.model.member.refresh_token.RefreshTokenRQ;
import com.travel.jeju.model.member.refresh_token.RefreshTokenRS;
import com.travel.jeju.util.Principal;
import com.travel.jeju.util.RandomUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    private final BCryptPasswordEncoder pwdEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsSecurityService userDetailsSecufityService;

	private short SMS_AUTH_TIME = 5; // 문자 인증시간 5분

    public SelectProfileRS profile() {

        String username = Principal.getUsername();

        if(username == null || username.trim().equals("")) throw new AppException(ExceptionCode.DATA_NO);

        MemberDto memberDto = memberMapper.memberSelect(username);

        ProfileModel profile = new ProfileModel();
        profile.setUsername(memberDto.getM_username());
        profile.setName(memberDto.getM_name());
        profile.setGender(memberDto.getM_gender());
        profile.setNickname(memberDto.getM_nickname());
        profile.setBirth(memberDto.getM_birth());
        profile.setPhone(memberDto.getM_phone());
        profile.setAddr(memberDto.getM_addr());
        profile.setEmail(memberDto.getM_email());
        profile.setCategory(memberDto.getM_category());
        
        SelectProfileRS result = new SelectProfileRS();
        result.setMemberProfile(profile);

        return result;
    }

    public RefreshTokenRS refreshToken(RefreshTokenRQ rq) {
        try {
            String refreshToken = rq.getRefresh_token();
            String username = jwtTokenProvider.getSubject(refreshToken);

            MemberDto dto = memberMapper.memberSelect(username);
            TokenBlackDto tokenBlackDto = new TokenBlackDto();
            tokenBlackDto.setUsername(username);
            tokenBlackDto.setRefresh_token(refreshToken);


            boolean refreshTokenValid = jwtTokenProvider.validateToken(refreshToken);
            boolean refreshTokenDBValid = dto.getM_refresh_token().equals(rq.getRefresh_token());
            boolean refreshTokenBlackValid = memberMapper.refreshTokenValid(tokenBlackDto) == 0 ? true : false;

            boolean refreshCheck = refreshTokenValid && refreshTokenDBValid && refreshTokenBlackValid; // 리프레쉬토큰 검증과, refreshTokenBlackValid 둘다 통과해야 Access토큰 재발급

            if(!refreshCheck) throw new AppException(ExceptionCode.SING_IN_FROM_ANOTHER_DEVICE);

            UserDetails userDetails = userDetailsSecufityService.loadUserByUsername(username);
            TokenModel tokenModel = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));
            
            RefreshTokenRS result = new RefreshTokenRS();
            result.setAccess_token(tokenModel.getAccessToken());

            return result;
        } catch (Exception e) {
            log.error("", e);

            if(e instanceof ExpiredJwtException){
                throw new AppException(ExceptionCode.REFRESH_JWT_EXPIRED);
            }

            throw e;

        }
    }

    @Transactional
    public boolean logout() {
        String username = Principal.getUsername();
        
        MemberDto dto = memberMapper.memberSelect(username);

        if(dto.getM_refresh_token() == null) throw new AppException(ExceptionCode.ALREADY_LOGOUT);
        
        String refreshToken = dto.getM_refresh_token();

        boolean result = true;

        try {
            TokenBlackDto tokenBlackModel = new TokenBlackDto();
            tokenBlackModel.setUsername(username);
            tokenBlackModel.setRefresh_token(refreshToken);

            memberMapper.updateLogout(username);
            memberMapper.insertTokenBlack(tokenBlackModel);
        } catch (Exception e) {
            result = false;

            log.error("",e);
        }

        return result;
    }

	public boolean certSms(CertSmsRQ rq) {

		int certValid = memberMapper.certValid(rq.getPhone_num());
        if(certValid != 0) throw new AppException(ExceptionCode.ALREADY_CERT);

        SmsDto smsDto = memberMapper.selectCertMessageBas(rq.getPhone_num());

        LocalDateTime now = LocalDateTime.now().minusMinutes(SMS_AUTH_TIME);
        LocalDateTime certTime = smsDto.getCreate_dt();

        boolean timeCheck = now.isBefore(certTime);

        if(!timeCheck) throw new AppException(ExceptionCode.TIME_OUT);
        if(!(smsDto.getCert_num().equals(rq.getCert_num()))) throw new AppException(ExceptionCode.NOT_AUTHENTICATION_USER);

        smsDto.setCert_yn(true);
        boolean result = memberMapper.updateSms(smsDto.getIdx());

        return result;
    }

	public boolean sendSms(String phone_num) {

		log.warn("phone_num => {}", phone_num);

        int certValid = memberMapper.certValid(phone_num);
        if(certValid != 0) throw new AppException(ExceptionCode.ALREADY_CERT);

        int[] randomArray = RandomUtil.randomIntegerArray(9, 6);

        String certNum = Arrays.toString(randomArray).replaceAll("[^0-9]", "");
        String msg = this.certMessageTemplate(certNum);

        SmsDto smsDto = new SmsDto();
        smsDto.setIdx(null);
        smsDto.setType(AuthConstants.REG_CERTIFICATE.getValue());
        smsDto.setContent(msg);
        smsDto.setCert_num(certNum);
        smsDto.setPhone_num(phone_num);
        smsDto.setCert_yn(false);

        boolean dbInsertCheck = memberMapper.insertSms(smsDto);

        // TODO:: 문자메세지 보내는 기능
        boolean sendMessage = true;

        boolean result = dbInsertCheck && sendMessage;

        log.warn("인증번호 => [{}]", certNum);

        return result;
    }

	public boolean nickCheck(String m_nickname) {
		
		return memberMapper.nickCheck(m_nickname) == 0 ? true : false;
	}

	public boolean idCheck(String m_username) {
		
		return memberMapper.idCheck(m_username) == 0 ? true : false;
    }

    @Transactional
	public boolean memberInsert(InsertMemeberRQ rq) {
		log.info("service - memberInsert()");

		MemberDto dto = new MemberDto();
		dto.setM_username(rq.getM_username());
		dto.setM_pass(rq.getM_pass());
		dto.setM_name(rq.getM_name());
		dto.setM_gender(rq.getM_gender());
		dto.setM_nickname(rq.getM_nickname());
		dto.setM_birth(rq.getM_birth());
		dto.setM_phone(rq.getM_phone());
		dto.setM_addr(rq.getM_addr());
		dto.setM_email(rq.getM_email());

		log.info("아이디 => {}", dto.getM_username());
		log.info("비번 => {}", dto.getM_pass());
		
		String encpwd = pwdEncoder.encode(dto.getM_pass());
		log.info("encpwd : " + encpwd);
		dto.setM_pass(encpwd);
		dto.setM_category(UserType.USER.getDesc());
		
		boolean result = true;

		try {
			memberMapper.memberInsert(dto);
			memberMapper.memberAuthoritiesInsert(dto);

		}catch (Exception e) {
			log.error("",e);
			result = false;
		}

		return result;
	}

	private String certMessageTemplate(String certNum){

        StringBuilder sb = new StringBuilder();

        sb.append("인증번호");
        sb.append("[");
        sb.append(certNum);
        sb.append("]");

        sb.append(" 입니다. 타인에게 알려주지 마세요.");

        return sb.toString();
    }
}
