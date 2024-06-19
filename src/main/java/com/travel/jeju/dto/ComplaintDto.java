package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ComplaintDto {

   private int co_pk_conum;

   private String co_fk_id;

   private String co_title;

   private String co_contents;

   private LocalDateTime co_create_dt;

   private boolean co_check;

   private String co_reply;

   private LocalDateTime co_re_create_dt;

}