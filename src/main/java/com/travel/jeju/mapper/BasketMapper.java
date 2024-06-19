package com.travel.jeju.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.BasketDto;

@Mapper
public interface BasketMapper {

    // basket 테이블 Insert
   public void insertBasket(Map<String, Object> omap);

   //bk_pk_num으로 basket 테이블 delete
   public void deleteBasketById(String bk_pk_num);

    public List<BasketDto> selectLodCompany(String trnum);
    // trnum에 맞는 데이터중 c_category가 레저인것만 select
    public List<BasketDto> selectActCompany(String trnum);
    // trnum에 맞는 데이터중 c_category가 식당인것만 select
    public List<BasketDto> selectFoodCompany(String trnum);

    public int selectMaxId();

    public boolean deleteBasketByLast(int value);

}
