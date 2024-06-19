package com.travel.jeju.model.main.home;

import lombok.Data;

import java.util.List;

import com.travel.jeju.dto.NoticeDto;

@Data
public class HomeNoticeRS {

    private List<NoticeDto> notice;
    
}
