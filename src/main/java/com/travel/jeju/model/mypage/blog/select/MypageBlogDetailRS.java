package com.travel.jeju.model.mypage.blog.select;

import java.util.List;

import com.travel.jeju.dto.BlogDto;
import com.travel.jeju.dto.TravelPlanDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.model.common.FileModel;
import com.travel.jeju.model.travel.plan.DaysModel;

import lombok.Data;

@Data
public class MypageBlogDetailRS {

    private BlogDto blog;    

    private List<FileModel> files;

    private FileModel mainFile;

    private DaysModel[] planList;

    private TravelrouteDto travelroute;
    
}
