package com.travel.jeju.model.main.business;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Alias("businessPlaceModel")
public class BusinessPlaceModel {

    private Integer c_pk_num;

    private String c_cnum;

    private String c_name;

    private String c_phone;

    private String c_category;

    private String c_addr;

    private String c_type;

    private Double c_lat;

    private Double c_lon;

    @JsonIgnore
    private Integer c_file_group_no;

    private BusinessFileModel fileData;
}
