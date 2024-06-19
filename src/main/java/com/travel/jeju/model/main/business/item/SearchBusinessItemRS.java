package com.travel.jeju.model.main.business.item;

import java.util.List;

import com.travel.jeju.model.common.FileModel;
import com.travel.jeju.model.main.business.BusinessFileModel;
import com.travel.jeju.model.main.business.BusinessItemModel;
import com.travel.jeju.model.main.business.BusinessPlaceModel;

import lombok.Data;

@Data
public class SearchBusinessItemRS {

    private BusinessPlaceModel company;

    private List<FileModel> companyFileList;

    private List<BusinessItemModel> companyItemList;

}
