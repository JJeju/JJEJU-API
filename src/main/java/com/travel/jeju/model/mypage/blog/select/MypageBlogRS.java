package com.travel.jeju.model.mypage.blog.select;

import java.util.List;

import com.travel.jeju.dto.BlogDto;

import lombok.Data;

@Data
public class MypageBlogRS {

    private List<BlogDto> blog;
    
}
