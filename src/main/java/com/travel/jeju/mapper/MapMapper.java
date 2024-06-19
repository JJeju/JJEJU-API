package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.CompanyDto;
@Mapper
public interface MapMapper {

	List<CompanyDto> getCategoryList(String c_category);

}
