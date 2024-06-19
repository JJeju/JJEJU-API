package com.travel.jeju.model.mypage.blog.select;

import lombok.Data;

@Data
public class MypageBlogParam {

    private String username;

    private String title;

    private int pageNum;

    private int listCnt;

}
