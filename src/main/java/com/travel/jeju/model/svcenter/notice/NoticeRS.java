package com.travel.jeju.model.svcenter.notice;

import java.util.List;

import com.travel.jeju.dto.NoticeDto;

import lombok.Data;

@Data
public class NoticeRS {

    private List<NoticeDto> notice;
    
}
