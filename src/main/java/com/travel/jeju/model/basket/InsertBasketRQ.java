package com.travel.jeju.model.basket;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InsertBasketRQ {

    private Integer bk_fk_cnum;

    private int bk_fk_num;

    private int bk_fk_tnum;

    private String bk_goods;

    private int bk_people;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime date;
    
}
