package com.travel.jeju.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.model.mypage.blog.create.CreateBlogRQ;
import com.travel.jeju.model.mypage.blog.select.MypageBlogDetailRS;
import com.travel.jeju.model.mypage.blog.select.MypageBlogRQ;
import com.travel.jeju.model.mypage.blog.select.MypageBlogRS;
import com.travel.jeju.model.mypage.blog.update.MypageBlogAddPictureRQ;
import com.travel.jeju.model.mypage.blog.update.MypageBlogUpdateRQ;
import com.travel.jeju.model.mypage.deal.MypageDealRQ;
import com.travel.jeju.model.mypage.deal.MypageDealRS;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteRS;
import com.travel.jeju.model.mypage.home.MypageHomeRS;
import com.travel.jeju.model.mypage.member.MypageMemberPasswordCheckRQ;
import com.travel.jeju.model.mypage.member.MypageMemberPasswordUpdateRQ;
import com.travel.jeju.model.mypage.member.MypageMemberUpdateRQ;
import com.travel.jeju.model.mypage.review.MypageReviewRS;
import com.travel.jeju.model.mypage.travel.MypageTravelDetailRS;
import com.travel.jeju.model.mypage.travel.MypageTravelRS;
import com.travel.jeju.model.mypage.travel.MypageTravelUpdateComplateRQ;
import com.travel.jeju.service.MypageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    /**
     * 마이페이지 첫 화면 [최근거래내역, 후기글 조회됨]
     * @return
     */
    @GetMapping("/home")
    public ResponseEntity<? extends BaseModel> mypageHome(){

        MypageHomeRS result = mypageService.mypageHome();

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 거래내역 조회
     * @param rq
     * @return
     */
    @GetMapping("/deal")
    public ResponseEntity<? extends BaseModel> deal(MypageDealRQ rq){

        MypageDealRS result = mypageService.deal(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 즐겨찾기 조회
     * @param category
     * @return
     */
    @GetMapping("/favorite")
    public ResponseEntity<? extends BaseModel> favorite(String category){

        MypageFavoriteRS result = mypageService.favorite(category);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 리뷰 조회
     * @param category
     * @return
     */
    @GetMapping("/review")
    public ResponseEntity<? extends BaseModel> review(String category){

        MypageReviewRS result = mypageService.review(category);
        
        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 후기글[블로그] 조회
     * @param rq
     * @return
     */
    @GetMapping("/blog")
    public ResponseEntity<? extends BaseModel> blog(MypageBlogRQ rq){

        MypageBlogRS result = mypageService.blog(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 후기글 상세조회
     * @param bnum
     * @return
     */
    @GetMapping("/blog/{b_pk_bnum}")
    public ResponseEntity<? extends BaseModel> blogDetail(@PathVariable(name = "b_pk_bnum") int bnum){

        MypageBlogDetailRS result = mypageService.blogDetail(bnum);
        


        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 마이페이지 후기글 추가
     * Multipart/Form-data
     * @param rq
     * @return
     */
    @PostMapping("/blog/create")
    public ResponseEntity<? extends BaseModel> blogCreate(@Valid CreateBlogRQ rq){

        boolean result = mypageService.blogCreate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 블로그 업데이트 [메인 대문사진 변경가능]
     * @param rq
     * @return
     */
    @PutMapping("/blog/update")
    public ResponseEntity<? extends BaseModel> blogUpdate(MypageBlogUpdateRQ rq){

        boolean result = mypageService.blogUpdate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 블로그 사진 추가
     * @param rq
     * @return
     */
    @PostMapping("/blog/add/picture")
    public ResponseEntity<? extends BaseModel> blogAddPicture(MypageBlogAddPictureRQ rq){

        boolean result = mypageService.blogAddPicture(rq);
        
        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 블로그 파일 삭제
     * @param file_idx
     * @return
     */
    @DeleteMapping("/blog/delete/picture/{file_idx}")
    public ResponseEntity<? extends BaseModel> blogDeletePicture(@PathVariable(name = "file_idx")int file_idx){

        boolean result = mypageService.blogDeletePicture(file_idx);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }
    /**
     * 블로그[후기글] 삭제 
     * @param b_pk_num
     * @return
     */
    @DeleteMapping("/blog/delete/{b_pk_num}")
    public ResponseEntity<? extends BaseModel> blogDelete(@PathVariable(name = "b_pk_num") int b_pk_num){

        boolean result = mypageService.blogDelete(b_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 회원 개인정보 수정 [생년월일, 이메일]
     * @param rq
     * @return
     */
    @PutMapping("/member/update")
    public ResponseEntity<? extends BaseModel> memberUpdate(@RequestBody MypageMemberUpdateRQ rq){

        boolean result = mypageService.memberUpdate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 패스워드 체크 [성공 : ture, 실패 : false]
     * @param rq
     * @return
     */
    @PostMapping("/member/password/check")
    public ResponseEntity<? extends BaseModel> memberPasswordCheck(@RequestBody MypageMemberPasswordCheckRQ rq){

        boolean result = mypageService.memberPasswordCheck(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 패스워드 업데이트
     * @param rq
     * @return
     */
    @PutMapping("/member/password/update")
    public ResponseEntity<? extends BaseModel> memberPasswordUpdate(@RequestBody MypageMemberPasswordUpdateRQ rq){

        boolean result = mypageService.memberPasswordUpdate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 리뷰 삭제하기
     * @param r_num
     * @return
     */
    @DeleteMapping("/review/delete")
    public ResponseEntity<? extends BaseModel> reviewDelete(@RequestParam Integer r_num){

        boolean result = mypageService.reviewDelete(r_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여정 상세보기
     * @param tr_pk_num
     * @return
     */
    @GetMapping("/travel/{tr_pk_num}")
    public ResponseEntity<? extends BaseModel> travelDetail(@PathVariable(name = "tr_pk_num")int tr_pk_num){

        MypageTravelDetailRS result = mypageService.travelDetail(tr_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여정 삭제하기
     * @param tr_pk_num
     * @return
     */
    @DeleteMapping("/travel/delete/{tr_pk_num}")
    public ResponseEntity<? extends BaseModel> travelDelete(@PathVariable(name = "tr_pk_num")int tr_pk_num){

        boolean result = mypageService.travelDelete(tr_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여정 완료하기
     * @param rq
     * @return
     */
    @PutMapping("/travel/update/complate")
    public ResponseEntity<? extends BaseModel> travleUpdateComplate(@RequestBody MypageTravelUpdateComplateRQ rq){

        boolean result = mypageService.travleUpdateComplate(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 여정 조회하기
     * @param tr_tf
     * @return
     */
    @GetMapping("/travel")
    public ResponseEntity<? extends BaseModel> travel(Boolean tr_tf) {

        MypageTravelRS result =  mypageService.travel(tr_tf);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 즐겨찾기 삭제
     * @param fa_pk_num
     * @return
     */
    @DeleteMapping("/favorite/delete/{fa_pk_num}")
    public ResponseEntity<? extends BaseModel> favoriteDelete(@PathVariable(name = "fa_pk_num") int fa_pk_num){

        boolean result = mypageService.favoriteDelete(fa_pk_num);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }


}
