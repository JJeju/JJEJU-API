package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.TravelPlanDto;
import com.travel.jeju.dto.TravelrouteDto;

@Mapper
public interface TravelrouteMapper {
   
   // 여행 테이블 insert
   public void insertTravel(TravelrouteDto tra);

   public boolean deleteTravelById(Integer tr_pk_num);

   public boolean deleteTravelByTitle(String title);

   public boolean insertTravelPlan(List<TravelPlanDto> params);

   public List<Integer> selectMyTravelNumber(String username);

   public TravelrouteDto selectTravelById(Integer tr_pk_num);

   public TravelPlanDto selectTravelPlanById(Integer tp_pk_num);

   public List<TravelPlanDto> selectTravelPlanByPlanNum(Integer tr_pk_num);

   public boolean deleteTravelPlanById(Integer tp_pk_num);

   public boolean updateTravelPlan(TravelPlanDto param);

   // 여행 테이블 tr_pk_num 으로 select
   public TravelrouteDto traSelect(String trnum);

}