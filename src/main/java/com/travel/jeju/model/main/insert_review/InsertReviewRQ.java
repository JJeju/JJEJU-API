package com.travel.jeju.model.main.insert_review;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class InsertReviewRQ {

    private Integer rv_start;

    private String rv_contents;

    private Integer fv_fk_cnum;

    private MultipartFile r_img;
}
