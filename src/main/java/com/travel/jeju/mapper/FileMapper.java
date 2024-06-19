package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.model.common.FileModel;

@Mapper
public interface FileMapper {

    public boolean deleteFile(int idx);

    public FileModel selectFileByFileGroupNoMain(int fileGroupNo);

    public List<FileModel> selectFileByFileGroupNo(int fileGroupNo);

    public  void saveAll(List<FileModel> comFileBas);

    public List<FileModel> selectFileByFileGroupNoAndNotMain(int fileGroupNo);

    public FileModel findFirstByOrderByFileGroupNoDesc();


}
