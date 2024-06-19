package com.travel.jeju.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.travel.jeju.constant.FileConstant;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.mapper.FileMapper;
import com.travel.jeju.model.common.FileModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUtil {

    // private final ComFileBasRepository comFileBasRepository;    

    private final FileMapper fileMapper;
    
    @Value("${url.base.file}")
    private String BASE_URL;

    /**
     * 파일 업데이트
     * @param fileGroupNo
     * @param multipartFiles
     * @param desc
     * @return
     */
    @Transactional
    public int updateFile(int fileGroupNo, List<MultipartFile> multipartFiles, String desc){

        boolean firstInsertCheck = fileGroupNo == 0 ? true : false;

        if(firstInsertCheck) {

           fileGroupNo = this.saveFile(multipartFiles, desc);

        } else {

            FileModel fileModel =  fileMapper.selectFileByFileGroupNoMain(fileGroupNo);

            if(fileModel != null){
                fileMapper.deleteFile(fileModel.getIdx());
                this.deleteFile(fileModel.getUrl());
            }

            String fileUrl = new StringBuilder()
                .append(this.BASE_URL)
                .append(DateUtil.parseLocalDateToString(LocalDate.now(), "yyyyMMdd"))
                .append("/")
                .toString();

            List<FileModel> comFileBas = new ArrayList<>();

            for(MultipartFile file : multipartFiles){
                FileModel node = this.fileUpload(file, fileUrl, fileGroupNo, desc);

                comFileBas.add(node);
            }

            fileMapper.saveAll(comFileBas);
        }
        

        return fileGroupNo;
    }

    /**
     * 로컬에 있는 물리 파일 삭제
     * @param url
     * @return
     */
    public boolean deleteFile(String url){
        File file = new File(url);

        boolean result = file.delete();

        return result;
    }

    @Transactional
    public int saveFile(List<MultipartFile> multipartFiles, String desc){

        String fileUrl = new StringBuilder()
                .append(this.BASE_URL)
                .append(DateUtil.parseLocalDateToString(LocalDate.now(), "yyyyMMdd"))
                .append("/")
                .toString();

        int fileGroupNo = this.lastFileGroupNo();

        List<FileModel> comFileBas = new ArrayList<>();

        for(MultipartFile file : multipartFiles){
            FileModel node = this.fileUpload(file, fileUrl, fileGroupNo, desc);

            comFileBas.add(node);
        }

        fileMapper.saveAll(comFileBas);

        return fileGroupNo;
    }

    @Transactional
    public int saveFile(int file_group_no, List<MultipartFile> multipartFiles, String desc){

        String fileUrl = new StringBuilder()
                .append(this.BASE_URL)
                .append(DateUtil.parseLocalDateToString(LocalDate.now(), "yyyyMMdd"))
                .append("/")
                .toString();

        int fileGroupNo = this.lastFileGroupNo();

        List<FileModel> comFileBas = new ArrayList<>();

        for(MultipartFile file : multipartFiles){
            FileModel node = this.fileUpload(file, fileUrl, fileGroupNo, desc);
            node.setFile_group_no(file_group_no);

            comFileBas.add(node);
        }

        fileMapper.saveAll(comFileBas);

        return fileGroupNo;
    }

     /**
     * 마지막 fileGroupNo 가져와 +1 후 반환
     *
     * @return
     */
    private Integer lastFileGroupNo() {
        FileModel lastComFileBas = fileMapper.findFirstByOrderByFileGroupNoDesc();
        int fileGroupNo = (lastComFileBas == null) ? 1 : lastComFileBas.getFile_group_no() + 1;

        return fileGroupNo;
    }

    /**
     * 파일 업로드
     * @param file
     * @param fileUrl
     * @param fileGroupNo
     * @return
     */
    private FileModel fileUpload(MultipartFile file, String fileUrl, int fileGroupNo, String desc){

        if(file == null) throw new AppException(ExceptionCode.FILE_ERROR);

        String oriNm = file.getOriginalFilename();
        String extension = oriNm.substring(oriNm.lastIndexOf("."));
        String etc = String.valueOf(System.currentTimeMillis()); // 동명이인 방지
        String fileName = this.getFileName(extension, etc);

        try {
            Path directoryPath = Paths.get(fileUrl);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            // 파일 저장 경로
            String filePath = fileUrl + fileName;

            File saveFile = new File(filePath);
            file.transferTo(saveFile);

            double fileSize = Files.size(Paths.get(filePath)) / 1024;

            FileModel node = new FileModel();
            node.setFile_group_no(fileGroupNo);
            node.setUrl(filePath.replaceAll(this.BASE_URL, ""));
            node.setFile_ori_nm(oriNm);
            node.setFile_sys_nm(fileName);
            node.setExtension(extension);
            node.setDescription(desc);
            node.setCreate_dt(LocalDateTime.now());
            node.setFile_size(fileSize);
            node.setFile_size_unit(FileConstant.KB.name());

            return node;
        } catch (IOException e) {
            log.error("",e);
            throw new AppException(ExceptionCode.FILE_ERROR);
        }
    }

    public String getFileName(String extension, String... etcName){

        String dateTime = DateUtil.parseLocalDateTimeToString(LocalDateTime.now(), "yyyyMMddHHmmss");

        StringBuilder result = new StringBuilder();
        result.append(dateTime);

        for (String name : etcName) {
            result.append("-")
                    .append(name);
        }

        result.append(extension);

        return result.toString();
    }
}
