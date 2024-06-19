package com.travel.jeju.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.model.basket.InsertBasketRQ;
import com.travel.jeju.model.basket.InsertBasketReserveRQ;
import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.service.BasketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    /**
     * 예약건 일정 담기
     * @param basdto
     * @param trnum
     * @param cnum
     * @param rname
     * @return
     */
    @PostMapping("/insert_basket_reserve")
    public ResponseEntity<? extends BaseModel> insertBasketReserve(@RequestBody InsertBasketReserveRQ rq) {

        boolean result = basketService.basketReserveInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 예약건 없는 일정 담기
     * @param bdto
     * @param date
     * @return
     */
    @PostMapping("/insert_basket")
    public ResponseEntity<? extends BaseModel> insertBasket(@RequestBody InsertBasketRQ rq){

        boolean result = basketService.basketInsert(rq); // TODO :: 결제정보가 없어 결제 관련내용 개발불가 , 추후 결제 추가시 다시 재개발 할 것.

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 장바구니 목록 삭제
     * @param bk_pk_num
     * @return
     */
    @DeleteMapping("/delete_basket")
    public ResponseEntity<? extends BaseModel> deleteBasket(String trnum,String bk_pk_num){

        Map<String, Object> result = basketService.basketDelete(trnum, bk_pk_num); // TODO :: 결제정보가 없어 결제 관련내용 개발불가 , 추후 결제 추가시 다시 재개발 할 것.

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

}
