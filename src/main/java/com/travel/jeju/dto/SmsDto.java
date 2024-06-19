package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SmsDto {

    private Integer idx; // 문자 일련번호

    private String type; // 문자 타입[보낸 타입 (인증, 안내 등등..)]

    private String content; // 문자 내용

    private String cert_num; // 인증번호

    private String phone_num; // 발신 휴대폰번호

    private Boolean cert_yn; // 인증여부

    private LocalDateTime create_dt; // 생성일시
}
