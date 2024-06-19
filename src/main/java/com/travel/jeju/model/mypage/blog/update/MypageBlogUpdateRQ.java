package com.travel.jeju.model.mypage.blog.update;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MypageBlogUpdateRQ {

    private Integer b_pk_num;

    private MultipartFile b_img;

    private String b_title;

    private String b_contents;

}
