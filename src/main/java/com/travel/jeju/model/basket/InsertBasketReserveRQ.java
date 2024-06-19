package com.travel.jeju.model.basket;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InsertBasketReserveRQ {

    private int bk_people;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime bk_in;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime bk_out;

    private String bk_number;

    private String bk_name;

    private int bk_fk_num;

    private String bk_carch;

    private Integer bk_fk_cnum;

    private String bk_goods;

    private int bk_fk_tnum;
}
