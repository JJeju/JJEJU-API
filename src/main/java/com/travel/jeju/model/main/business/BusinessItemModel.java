package com.travel.jeju.model.main.business;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("businessItemModel")
public class BusinessItemModel {

    private Integer idx;

    private Integer p_fk_cnum;

    private String p_name;

    private Integer p_price;

    private String p_content;

    private Boolean p_reserver_status;

    private Boolean p_exposure;

    private Integer file_group_no;

    private LocalDateTime create_dt;

    private LocalDateTime update_dt;

    private BusinessFileModel fileData;

}
