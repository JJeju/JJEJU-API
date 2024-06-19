package com.travel.jeju.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.FavoritesDto;
import com.travel.jeju.model.basket.InsertBasketRQ;
import com.travel.jeju.model.basket.InsertBasketReserveRQ;
import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.model.main.business.item.SearchBusinessItemDetailRS;
import com.travel.jeju.model.main.business.item.SearchBusinessItemRS;
import com.travel.jeju.model.main.business.place.SelectBusinessPlaceRQ;
import com.travel.jeju.model.main.business.place.SelectBusinessPlaceRS;
import com.travel.jeju.model.main.home.HomeBusinessRS;
import com.travel.jeju.model.main.home.HomeComplaintRS;
import com.travel.jeju.model.main.home.HomeEventSpotRS;
import com.travel.jeju.model.main.home.HomeNoticeRS;
import com.travel.jeju.model.main.insert_favorite.InsertFavoriteRQ;
import com.travel.jeju.model.main.insert_review.InsertReviewRQ;
import com.travel.jeju.model.main.option_check.OptionCheckRQ;
import com.travel.jeju.model.main.public_blog.SelectPublicBlogRS;
import com.travel.jeju.model.main.update_pay.UpdatePayRQ;
import com.travel.jeju.service.MainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    /**
     * 메인 홈화면 페이지에 필요한 리스트들
     * @return
     */
    @GetMapping("/home")
    public ResponseEntity<? extends BaseModel> home(){

        Map<String, Object> result = mainService.homeList();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 메인 홈화면, 랜덤 숙박,레저,식당 리스트
     * @return
     */
    @GetMapping("/home/business_place")
    public ResponseEntity<? extends BaseModel> homeBusiness(){

        HomeBusinessRS result = mainService.homeBusiness();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 메인 홈화면, 이벤트 관광지
     * @return
     */
    @GetMapping("/home/event-spot")
    public ResponseEntity<? extends BaseModel> homeEventAndSpot(){

        HomeEventSpotRS result = mainService.homeEventAndSpot();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 메인 홈화면, 공지사항
     * @return
     */
    @GetMapping("/home/notice")
    public ResponseEntity<? extends BaseModel> homeNotice(){

        HomeNoticeRS result = mainService.homeNotice();
        
        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 메인 홈화면, 신고사항
     * @return
     */
    @GetMapping("/home/complaint")
    public ResponseEntity<? extends BaseModel> homeComplaint(){

        HomeComplaintRS result = mainService.homeComplaint();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 숙박, 레저, 식당 관련 영업장 리스트들을 카테고리에 맞춰 반환함
     * @param category
     * @return
     */
    @GetMapping("/business_place")
    public ResponseEntity<? extends BaseModel> businessPlace(SelectBusinessPlaceRQ rq) {

        SelectBusinessPlaceRS result = mainService.businessPlaceList(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 영업장의 아이템[방, 레저 등의 상품]을 가져옴
     * @param cnum // 사업장 번호
     * @return
     */
    @GetMapping("/business_item")   
    public ResponseEntity<? extends BaseModel> businessItem(Integer cnum) {

        SearchBusinessItemRS result = mainService.businessItem(cnum);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }


    /**
     * 영업장의 아이템[방, 레저 등의 상품]의 상세정보를 보여줌
     * @param rnum
     * @param cate
     * @return
     */
    @GetMapping("/item_infomation")
    public ResponseEntity<? extends BaseModel> itemInfomation(Integer itemNumber){

        SearchBusinessItemDetailRS result = mainService.item_infomation(itemNumber);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 결제 업데이트 TODO::추후 결제정보 추가할 것임. 현재는 결제된 시간, 결제 상태만 변경함
     * @param bdto
     * @return
     */
    @PostMapping("/update_pay")
    public ResponseEntity<? extends BaseModel> updatePay(@RequestBody UpdatePayRQ rq){

        boolean result = mainService.payUpdate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs); 
    }

    /**
     * 옵션에 맞는 업체정보 리스트 조회
     * @param arrList
     * @param cate
     * @return
     */
    @GetMapping("/option_check")
    public ResponseEntity<? extends BaseModel> optionCheck(OptionCheckRQ rq){

        Map<String, Object> result = mainService.optionCheck(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 리뷰 작성
     * @param multi
     * @return
     */
    @PostMapping("/insert_review")
    public ResponseEntity<? extends BaseModel> insertReview(InsertReviewRQ rq) {

        boolean result = mainService.reviewInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 즐겨찾기 추가
     * @param dto
     * @return
     */
    @PostMapping("/insert_favorite")
    public ResponseEntity<? extends BaseModel> insertFavorite(@RequestBody InsertFavoriteRQ rq){

        boolean result = mainService.favoritInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 메인에 보여질 공개여부가 true값인 데이터 랜덤으로 6개 가져오는 기능
     * @return
     */
    @GetMapping("/public_blog")
    public ResponseEntity<? extends BaseModel> getPublicBlog(){

        SelectPublicBlogRS result = mainService.getPublicBlog();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

}
