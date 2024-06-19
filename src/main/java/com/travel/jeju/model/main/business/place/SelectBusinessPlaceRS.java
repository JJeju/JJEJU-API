package com.travel.jeju.model.main.business.place;

import java.util.List;

import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.model.main.business.BusinessPlaceModel;

import lombok.Data;

@Data
public class SelectBusinessPlaceRS {

    private List<BusinessPlaceModel> companyRandomList;
}
