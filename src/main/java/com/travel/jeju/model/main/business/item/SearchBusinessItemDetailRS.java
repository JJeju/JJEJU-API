package com.travel.jeju.model.main.business.item;

import java.util.List;

import com.travel.jeju.model.main.business.BusinessFileModel;
import com.travel.jeju.model.main.business.BusinessItemModel;

import lombok.Data;

@Data
public class SearchBusinessItemDetailRS {

    private BusinessItemModel itemDetail;

    private List<BusinessFileModel> itemFileList;

    
}
