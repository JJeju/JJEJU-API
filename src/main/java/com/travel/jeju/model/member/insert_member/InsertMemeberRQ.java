package com.travel.jeju.model.member.insert_member;

import lombok.Data;

@Data
public class InsertMemeberRQ {

    private String m_username;

    private String m_pass;

    private String m_name;

    private String m_gender;

    private String m_nickname;

    private String m_birth;

    private String m_phone;

    private String m_addr;

    private String m_email;
}
