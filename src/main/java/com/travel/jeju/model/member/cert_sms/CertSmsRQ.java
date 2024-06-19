package com.travel.jeju.model.member.cert_sms;

import lombok.Data;

@Data
public class CertSmsRQ {

    private String phone_num;

    private String cert_num;
}
