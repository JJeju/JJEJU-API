package com.travel.jeju.model.mypage.travel;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("mypageTravelParam")
public class MypageTravelParam {

    private String username;

    private Boolean tr_tf;

}
