package com.travel.jeju.model.mypage.deal;

import lombok.Data;

@Data
public class MypageDealParam {

    private String username;

    private String category; // 장바구니 결제 영업장 카테고리[숙박, 레저]

    private String name; // 영업장 이름

    private int pageNum; // 페이지 번호

    private int listCnt; // ListSize max 값
}
