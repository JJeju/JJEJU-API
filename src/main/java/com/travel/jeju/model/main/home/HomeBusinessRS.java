package com.travel.jeju.model.main.home;

import java.util.List;

import com.travel.jeju.dto.CompanyDto;

import lombok.Data;

@Data
public class HomeBusinessRS {

    private List<CompanyDto> lodgment;

    private List<CompanyDto> leisure;

    private List<CompanyDto> restaurant;

}
