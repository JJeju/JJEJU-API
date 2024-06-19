package com.travel.jeju.model.mypage.blog.create;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBlogRQ {

    @NotNull
    private Integer tnum; // 후기글 작성할 여정번호

    @NotNull
    private String title; // 후기 제목

    private Integer cost; // 여정 총 비용

    private String contents; // 후기 내용

    @NotNull
    @Max(5)
    @Min(0)
    private Integer star; // 별점 [ 작성자 본인의 만족도 ]

    @NotNull
    private Boolean publicCheck; // 공개여부

    private MultipartFile mainFile; 

    private List<MultipartFile> files; // 사진들
}
