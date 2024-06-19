package com.travel.jeju.model.common;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileModel {

    private Integer idx;

    private Integer file_group_no;

    private String url;

    private String file_ori_nm;

    private String file_sys_nm;

    private String extension;

    private String description;

    private double file_size;

    private String file_size_unit;

    private LocalDateTime create_dt;

}
