package com.travel.jeju.model.main.business;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import com.travel.jeju.model.common.FileModel;

import lombok.Data;

@Data
@Alias("businessFileModel")
public class BusinessFileModel {
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

    public static BusinessFileModel toRSModel(FileModel model){
        BusinessFileModel result = new BusinessFileModel();
        result.setIdx(model.getIdx());
        result.setFile_group_no(model.getFile_group_no());
        result.setUrl(model.getUrl());
        result.setFile_ori_nm(model.getFile_ori_nm());
        result.setFile_sys_nm(model.getFile_sys_nm());
        result.setExtension(model.getExtension());
        result.setDescription(model.getDescription());
        result.setFile_size(model.getFile_size());
        result.setFile_size_unit(model.getFile_size_unit());
        result.setCreate_dt(model.getCreate_dt());

        return result;
    }
}
