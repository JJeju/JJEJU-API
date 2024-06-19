package com.travel.jeju.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.model.member.cert_sms.CertSmsRQ;
import com.travel.jeju.model.member.insert_member.InsertMemeberRQ;
import com.travel.jeju.model.member.profile.SelectProfileRS;
import com.travel.jeju.model.member.refresh_token.RefreshTokenRQ;
import com.travel.jeju.model.member.refresh_token.RefreshTokenRS;
import com.travel.jeju.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * @param dto
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<? extends BaseModel> memberInsert(@RequestBody InsertMemeberRQ rq){

        boolean result = memberService.memberInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * [리프레쉬]액세스토큰 재발급
     * @param rq
     * @return
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<? extends BaseModel> refreshToken(@RequestBody RefreshTokenRQ rq){

        RefreshTokenRS result = memberService.refreshToken(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 로그아웃 [리프레쉬토큰 사용불가항목에 추가]
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<? extends BaseModel> logout(){

        boolean result = memberService.logout();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 아이디 중복확인 [true : 사용가능 , false : 사용불가]
     * @param m_username
     * @return
     */
    @GetMapping("/id_check")
    public ResponseEntity<? extends BaseModel> idCheck(String m_username){

        boolean result = memberService.idCheck(m_username);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 닉네임 중복확인 [true : 사용가능 , false : 사용불가]
     * @param m_nickname
     * @return
     */
    @GetMapping("/nick_check")
    public ResponseEntity<? extends BaseModel> nickCheck(String m_nickname){

        boolean result = memberService.nickCheck(m_nickname);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 휴대폰으로 문자메세지 발송
     * @param phone_num
     * @return
     */
    @GetMapping("/send_sms")
    public ResponseEntity<? extends BaseModel> sendSms(String phone_num){

        boolean result = memberService.sendSms(phone_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 인증문자 번호 확인
     * @param rq
     * @return
     */
    @PostMapping("/cert_sms")
    public ResponseEntity<? extends BaseModel> certSms(@RequestBody CertSmsRQ rq){

        boolean result = memberService.certSms(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 개인정보 조회
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<? extends BaseModel> profile(){

        SelectProfileRS result = memberService.profile();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);        
    }

}
