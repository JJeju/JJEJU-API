package com.travel.jeju.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.TravelPlanDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.mapper.CompanyMapper;
import com.travel.jeju.mapper.TravelrouteMapper;
import com.travel.jeju.model.travel.TravelInsertRQ;
import com.travel.jeju.model.travel.TravelInsertRS;
import com.travel.jeju.model.travel.plan.DaysModel;
import com.travel.jeju.model.travel.plan.InsertPlanModel;
import com.travel.jeju.model.travel.plan.SelectPlanModel;
import com.travel.jeju.model.travel.plan.TravelPlanArraySortObject;
import com.travel.jeju.model.travel.plan.TravelPlanInsertRQ;
import com.travel.jeju.model.travel.plan.TravelPlanSelectRS;
import com.travel.jeju.model.travel.plan.TravelPlanUpdateRQ;
import com.travel.jeju.model.travel.plan.UpdatePlanModel;
import com.travel.jeju.util.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripService {
    
    private final TravelrouteMapper travelrouteMapper;

    private final CompanyMapper companyMapper;

    public TravelPlanSelectRS travelPlanSelect(Integer tr_pk_num) {

        TravelrouteDto travel = travelrouteMapper.selectTravelById(tr_pk_num);
        String username = Principal.getUsername();

        if(travel == null) throw new AppException(ExceptionCode.DATA_NO);
        if(!travel.getTr_fk_id().equals(username)) throw new AppException(ExceptionCode.OTHER_USER_DATA);

        List<TravelPlanDto> travelPlanDtoList = travelrouteMapper.selectTravelPlanByPlanNum(tr_pk_num);

        final LocalDate tr_in = travel.getTr_in();
        final LocalDate tr_out = travel.getTr_out();
        
        DaysModel[] daysModels = this.createDaysArray(tr_in, tr_out);

        Map<Integer , CompanyDto> companyMap = new HashMap<>();

        for(TravelPlanDto dto : travelPlanDtoList){
            LocalDate planStartDate = dto.getTp_plan_start_date_time().toLocalDate();
            LocalDate planEndDate = dto.getTp_plan_end_date_time().toLocalDate();
            
            int mitnusStartDays = (int)ChronoUnit.DAYS.between(tr_in, planStartDate);
            int mitnusEndDays = (int)ChronoUnit.DAYS.between(tr_in, planEndDate);

            Integer cnum = dto.getTp_fk_company_num();

            CompanyDto companyNode = companyMap.get(cnum);

            if(companyNode == null){
                companyMap.put(cnum, companyMapper.selectCompanyById(cnum));
                companyNode = companyMap.get(cnum);
            }

            SelectPlanModel model = new SelectPlanModel();
            model.setTp_pk_num(dto.getTp_pk_num());
            model.setTp_fk_tnum(dto.getTp_fk_tnum());
            model.setTp_fk_company_info(companyNode);
            model.setTp_item_category(dto.getTp_item_category());
            model.setTp_plan_start_date_time(dto.getTp_plan_start_date_time());
            model.setTp_plan_end_date_time(dto.getTp_plan_end_date_time());
            model.setTp_rm(dto.getTp_rm());
            model.setCreate_dt(dto.getCreate_dt());
            model.setUpdate_dt(dto.getUpdate_dt());

            daysModels[mitnusStartDays].getDayPlanList().add(model);
            daysModels[mitnusEndDays].getDayPlanList().add(model);   
        }

        for(int i = 0; i < daysModels.length; i ++){
            List<SelectPlanModel> dayPlanList = daysModels[i].getDayPlanList();
            if(dayPlanList.size() == 1) continue;

            List<SelectPlanModel> newDayPlanList = new ArrayList<>();

            for(SelectPlanModel model : dayPlanList){
                if(!newDayPlanList.contains(model)){
                    newDayPlanList.add(model);
                }
            }

            newDayPlanList = this.sortingArrayByDate(newDayPlanList, tr_in.plusDays(i));

            daysModels[i].setDayPlanList(newDayPlanList);            
        }


        TravelPlanSelectRS result = new TravelPlanSelectRS();
        result.setPlanItemList(travelPlanDtoList);
        result.setPlanList(daysModels);
        result.setTravelroute(travel);

        return result;
    }

    private List<SelectPlanModel> sortingArrayByDate(List<SelectPlanModel> dayPlanList, LocalDate standardDate){
        
        List<TravelPlanArraySortObject> sortingList = new ArrayList<>();

        for(int i = 0 ; i < dayPlanList.size(); i ++){
            SelectPlanModel model = dayPlanList.get(i);

            TravelPlanArraySortObject node = new TravelPlanArraySortObject();
            node.setIndex(i);

            if(standardDate.isEqual(model.getTp_plan_start_date_time().toLocalDate())){
                node.setSortingDate(model.getTp_plan_start_date_time());
            } else {
                node.setSortingDate(model.getTp_plan_end_date_time());
            }

            sortingList.add(node);
        }

        Collections.sort(sortingList, Comparator.comparing(TravelPlanArraySortObject::getSortingDate));

        List<SelectPlanModel> newDayPlanList = new ArrayList<>();

        for(int i = 0; i < sortingList.size(); i++){

            newDayPlanList.add(dayPlanList.get(sortingList.get(i).getIndex()));
        }

        return newDayPlanList;
    }


    private DaysModel[] createDaysArray(LocalDate startDate, LocalDate endDate){
        
        int mitnusDays = (int)ChronoUnit.DAYS.between(startDate, endDate);

        DaysModel[] daysModels = new DaysModel[mitnusDays+1];

        for(int i = 0; i < daysModels.length; i ++){
            
            daysModels[i] = new DaysModel();
            daysModels[i].setDayPlanList(new ArrayList<>());
            daysModels[i].setDay(startDate);

            startDate = startDate.plusDays(1);
        }

        return daysModels;
    }

    @Transactional
    public boolean planUpdate(TravelPlanUpdateRQ rq) {

        TravelrouteDto travel = travelrouteMapper.selectTravelById(rq.getT_pk_num());
        String username = Principal.getUsername();

        if(travel == null) throw new AppException(ExceptionCode.DATA_NO);
        if(!travel.getTr_fk_id().equals(username)) throw new AppException(ExceptionCode.OTHER_USER_DATA);

        for(UpdatePlanModel model : rq.getUpdatePlanList()){

            // [ 유효성 검사 ]
            // 계획요소의 PK값이 실제로 있는것인지 확인
            TravelPlanDto travelPlanDto = travelrouteMapper.selectTravelPlanById(model.getTr_pk_num());
            if(travelPlanDto == null) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);

            // 계획요소의 날짜 값이 여행의 날짜에서 벗어나지는 않는지와 start날짜와 end날짜가 반대로 되어 있지는 않은지 확인
            if(model.getPlan_start_date_time().isAfter(model.getPlan_end_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            if(travel.getTr_in().atStartOfDay().isAfter(model.getPlan_start_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            if(travel.getTr_out().atStartOfDay().isBefore(model.getPlan_end_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            
            // Update할 데이터 추가 
            travelPlanDto.setTp_pk_num(model.getTr_pk_num());
            if(model.getPlan_start_date_time() != null) travelPlanDto.setTp_plan_start_date_time(model.getPlan_start_date_time());
            if(model.getPlan_end_date_time() != null) travelPlanDto.setTp_plan_end_date_time(model.getPlan_end_date_time());
            if(model.getRm() != null) travelPlanDto.setTp_rm(model.getRm());

            // Updqte할 데이터가 없으면 Update진행하지 않음.
            if(travelPlanDto.getTp_plan_start_date_time() == null && travelPlanDto.getTp_plan_end_date_time() == null && travelPlanDto.getTp_rm() == null) continue;

            travelPlanDto.setUpdate_dt(LocalDateTime.now());

            try {
                travelrouteMapper.updateTravelPlan(travelPlanDto);
            } catch (Exception e) {
                log.error("ERROR OBJECT => {}", travelPlanDto);
                log.error("", e);
    
                throw new AppException(ExceptionCode.DB_ERROR);
            }
            
        }
        
        boolean result = true;

        return result;
    }

    @Transactional
    public boolean planDelete(Integer tp_pk_num) {

        TravelPlanDto travelPlanDto = travelrouteMapper.selectTravelPlanById(tp_pk_num);

        if(travelPlanDto == null) return true;

        TravelrouteDto travelrouteDto = travelrouteMapper.selectTravelById(travelPlanDto.getTp_fk_tnum());
        String username = Principal.getUsername();

        if(!travelrouteDto.getTr_fk_id().equals(username)) throw new AppException(ExceptionCode.OTHER_USER_DATA);
        
        try {
            travelrouteMapper.deleteTravelPlanById(tp_pk_num);
        } catch (Exception e) {
            log.error("", e);

            throw new AppException(ExceptionCode.DB_ERROR);
        }

        return true;
    }

    @Transactional
    public boolean planInsert(TravelPlanInsertRQ rq) {

        TravelrouteDto travel = travelrouteMapper.selectTravelById(rq.getT_pk_num());
        String username = Principal.getUsername();

        if(travel == null) throw new AppException(ExceptionCode.DATA_NO);
        if(!travel.getTr_fk_id().equals(username)) throw new AppException(ExceptionCode.OTHER_USER_DATA);

        List<TravelPlanDto> insertParam = new ArrayList<>();

        for(InsertPlanModel model : rq.getPlanList()){
            if(model.getPlan_start_date_time().isAfter(model.getPlan_end_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            if(travel.getTr_in().atStartOfDay().isAfter(model.getPlan_start_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            if(travel.getTr_out().atStartOfDay().isBefore(model.getPlan_end_date_time())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);
            
            TravelPlanDto node = new TravelPlanDto();
            node.setTp_fk_tnum(rq.getT_pk_num());
            node.setTp_fk_company_num(model.getCompany_pk_num());
            node.setTp_item_category(model.getCategory());
            node.setTp_plan_start_date_time(model.getPlan_start_date_time());
            node.setTp_plan_end_date_time(model.getPlan_end_date_time());
            node.setTp_rm(model.getRm());
            node.setCreate_dt(LocalDateTime.now());
            node.setUpdate_dt(LocalDateTime.now());

            insertParam.add(node);
        }

        try {
            travelrouteMapper.insertTravelPlan(insertParam);
        } catch (Exception e) {
            log.error("", e);
            
            throw new AppException(ExceptionCode.DB_ERROR);
        }
        boolean result = true;
        
        return result;
    }

    @Transactional
    public TravelInsertRS travelInsert(TravelInsertRQ rq) {

        String username = Principal.getUsername();

        if(rq.getTr_in().isAfter(rq.getTr_out())) throw new AppException(ExceptionCode.NON_VALID_PARAMETER);

        TravelrouteDto travel = new TravelrouteDto();
        travel.setTr_fk_id(username);
        travel.setTr_title(rq.getTr_title());
        travel.setTr_in(rq.getTr_in());
        travel.setTr_out(rq.getTr_out());
        travel.setTr_relationship(rq.getTr_relationship());

        try {
            travelrouteMapper.insertTravel(travel);
        } catch (Exception e) {
            log.error("",e);
        }

        TravelInsertRS result = new TravelInsertRS();
        result.setCreateTravelPK(travel.getTr_pk_num());

        return result;
    }

}
