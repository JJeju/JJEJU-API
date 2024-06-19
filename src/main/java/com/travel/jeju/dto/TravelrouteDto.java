package com.travel.jeju.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TravelrouteDto {

	private int tr_pk_num;

	private String tr_fk_id;

	private String tr_title;

	private LocalDate tr_in;

	private LocalDate tr_out;

	private String tr_relationship;

	private boolean tr_tf;

}
