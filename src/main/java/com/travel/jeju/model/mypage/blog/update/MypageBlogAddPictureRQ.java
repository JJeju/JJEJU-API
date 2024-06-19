package com.travel.jeju.model.mypage.blog.update;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MypageBlogAddPictureRQ {

    private int b_pk_num;
    
    private MultipartFile b_img;
    
}
