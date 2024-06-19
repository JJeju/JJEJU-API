package com.travel.jeju.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class NoticeDto {
   private int n_pk_num;
   private String n_title;
   private String n_contents;
   private int n_views;
   private  LocalDateTime n_date;
}