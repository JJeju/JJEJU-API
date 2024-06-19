package com.travel.jeju.model.mypage.home;

import java.util.List;

import com.travel.jeju.dto.IamBasketDto;
import com.travel.jeju.dto.PastTripBlogDto;

import lombok.Data;

@Data
public class MypageHomeRS {

    private List<IamBasketDto> payment; // 결제내역

    private List<PastTripBlogDto> blog; // 후기

}
