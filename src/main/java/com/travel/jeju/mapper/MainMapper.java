package com.travel.jeju.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.AactivityDto;
import com.travel.jeju.dto.AoptionDto;
import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.BlogDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.ComplaintDto;
import com.travel.jeju.dto.EventDto;
import com.travel.jeju.dto.FavoritesDto;
import com.travel.jeju.dto.LoptionDto;
import com.travel.jeju.dto.NoticeDto;
import com.travel.jeju.dto.ReviewDto;
import com.travel.jeju.dto.SpotDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.model.main.business.BusinessItemModel;
import com.travel.jeju.model.main.business.BusinessPlaceModel;

@Mapper
public interface MainMapper {
   // cate에 따라 Company테이블 select
   public List<BusinessPlaceModel> selectCompanyByCategory(String category);
   // basket 테이블 Insert
   public void BasketInsert(Map<String, Object> omap);
   // trnum으로 trdto select
   public TravelrouteDto getTrdto(String trnum);
   // trnum에 맞는 데이터중 c_category가 숙박인것만 select
   public List<BasketDto> selectLodCompany(String trnum);
   // trnum에 맞는 데이터중 c_category가 레저인것만 select
   public List<BasketDto> selectActCompany(String trnum);
   // trnum에 맞는 데이터중 c_category가 식당인것만 select
   public List<BasketDto> selectFoodCompany(String trnum);

   public BusinessItemModel selectProductById(Integer id);
   
   
   // 결제후 bk_paych 1, bk_paytime 현재 시간으로 업데이트
   public void payUpdate(BasketDto bdto);
   // lodto에 따른 Company select
   public ArrayList<LoptionDto> loptionCheck(LoptionDto lodto);
   // aodto에 따른 Company select
   public ArrayList<AoptionDto> aoptionCheck(AoptionDto lodto);
   // review insert
   public void reviewInsert(ReviewDto rdto);
   // 사진이 있을시 파일컬럼인 rv_img를 sysname으로 update
   public void ReviewR_imgU(Map<String, String> fmap);

   public boolean updateReviewImg(ReviewDto rdto);
   
    //home QAlist
 	  // public List<ComplaintDto> homeqaListSelect();
   public List<ComplaintDto> selectHomeComplaint();
 	
    //home 공지사항List
    // public List<NoticeDto> homenoticeListSelect();

   public List<NoticeDto> selectHomeNotice();
 	
 	  //home 이벤트lsit
    public List<EventDto> selectHomeEvent();
    
    //home 숙박이미지뿌리기
    public List<CompanyDto> selectHomeLodgment();
    
    //home 레저이미지뿌리기
    public List<CompanyDto> selectHomeLeisure();

    //home 식당이미지뿌리기
    public List<CompanyDto> selectHomeRestaurant();
    
    //home md숙박이미지뿌리기
    public List<CompanyDto> homemdcompanyList();
    
    //home md레저이미지뿌리기
    public List<CompanyDto> homemdactiveList();
    //제주 관광지
    public List<SpotDto> spotbigList();

    public List<SpotDto> selectHomeSpot();

    public void favoritesInsert(FavoritesDto fav);

    public List<BlogDto> selectTop6RandomPublicBlog();
	

   
}