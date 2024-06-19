package com.travel.jeju.model.main.public_blog;

import java.util.List;

import com.travel.jeju.dto.BlogDto;

import lombok.Data;

@Data
public class SelectPublicBlogRS {

    private List<BlogDto> publicBlogList;

}
