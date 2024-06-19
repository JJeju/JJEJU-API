package com.travel.jeju.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.model.travel.TravelInsertRQ;
import com.travel.jeju.model.travel.TravelInsertRS;
import com.travel.jeju.model.travel.plan.TravelPlanInsertRQ;
import com.travel.jeju.model.travel.plan.TravelPlanSelectRS;
import com.travel.jeju.model.travel.plan.TravelPlanUpdateRQ;
import com.travel.jeju.service.TripService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/travel")
@Slf4j
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    /**
     * 여행건 만드는 기능
     * @param rq
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<? extends BaseModel> travelInsert(@RequestBody TravelInsertRQ rq){

        TravelInsertRS result = tripService.travelInsert(rq);
        
        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여행 계획요소 만들기 기능
     * @param rq
     * @return
     */
    @PostMapping("/plan/insert")
    public ResponseEntity<? extends BaseModel> planInsert(@RequestBody @Valid TravelPlanInsertRQ rq){

        boolean result = tripService.planInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여행 계획요소 삭제 기능
     * @param tp_pk_num
     * @return
     */
    @DeleteMapping("/plan/delete/{tp_pk_num}")
    public ResponseEntity<? extends BaseModel> planDelete(@PathVariable Integer tp_pk_num){

        boolean result = tripService.planDelete(tp_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 어행 계획요소 수정 기능
     * @param rq
     * @return
     */
    @PutMapping("/plan/update")
    public ResponseEntity<? extends BaseModel> planUpdate(@RequestBody TravelPlanUpdateRQ rq){

        boolean result = tripService.planUpdate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여행 번호로 여행 계획요소 가져오는 기능
     * @param tr_pk_num
     * @return
     */
    @GetMapping("/plan/select/{tr_pk_num}")
    public ResponseEntity<? extends BaseModel> travelPlanSelect(@PathVariable Integer tr_pk_num){

        TravelPlanSelectRS result = tripService.travelPlanSelect(tr_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }




}
