package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BasketDto {

	private Integer bk_pk_num;

	private Integer bk_fk_tnum;

	private Integer bk_fk_cnum;

	private String bk_fk_id;

	private Integer bk_fk_num;

	private String bk_goods;

	private Integer bk_price;

	private LocalDateTime bk_in;

	private LocalDateTime bk_out;

	private String bk_name;

	private String bk_number;

	private String bk_carch;

	private String bk_paych;

	private String bk_paytime;

	private Integer bk_people;

	private String c_name;

	private LocalDateTime bk_create_dt;
	
}
 