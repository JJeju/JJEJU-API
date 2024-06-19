package com.travel.jeju.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.dto.ComplaintDto;
import com.travel.jeju.dto.NoticeDto;
import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.model.svcenter.notice.NoticeRQ;
import com.travel.jeju.model.svcenter.notice.NoticeRS;
import com.travel.jeju.model.svcenter.qa.QaInsertRQ;
import com.travel.jeju.model.svcenter.qa.QaMyListRQ;
import com.travel.jeju.model.svcenter.qa.QaMyListRS;
import com.travel.jeju.model.svcenter.qa.QaRQ;
import com.travel.jeju.model.svcenter.qa.QaRS;
import com.travel.jeju.service.SvcenterServcie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/service-center")
@Slf4j
@RequiredArgsConstructor
public class SvcenterController {

    private final SvcenterServcie svcenterServcie;

    /**
     * 공지사항 리스트 조회
     * @param rq
     * @return
     */
    @GetMapping("/notice")
    public ResponseEntity<? extends BaseModel> notice(NoticeRQ rq) {

        NoticeRS result = svcenterServcie.notice(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 공지사항 상세조회
     * @param n_pk_num
     * @return
     */
    @GetMapping("/notice/{n_pk_num}")
    public ResponseEntity<? extends BaseModel> noticeDetail(@PathVariable(name = "n_pk_num") int n_pk_num){

        NoticeDto notice = svcenterServcie.noticeDetail(n_pk_num);

        BaseModel rs = new BaseModel(notice);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * Qa리스트 조회
     * @param rq
     * @return
     */
    @GetMapping("/qa")
    public ResponseEntity<? extends BaseModel> qa(QaRQ rq) {

        QaRS result = svcenterServcie.qa(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * Qa 추가하기
     * @param rq
     * @return
     */
    @PostMapping("/qa/insert")
    public ResponseEntity<? extends BaseModel> qaInsert(@RequestBody QaInsertRQ rq){

        boolean result = svcenterServcie.qaInsert(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * 나의 Qa 리스트 조회하기
     * @param pageNum
     * @return
     */
    @GetMapping("/qa/my-list")
    public ResponseEntity<? extends BaseModel> qaMyList(QaMyListRQ rq){

        QaMyListRS result = svcenterServcie.qaMyList(rq);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * Qa 상세보기
     * @param co_pk_conum
     * @return
     */
    @GetMapping("/qa/detail/{co_pk_conum}")
    public ResponseEntity<? extends BaseModel> qaDetail(@PathVariable(name = "co_pk_conum") int co_pk_conum){

        ComplaintDto complaint = svcenterServcie.qaDetail(co_pk_conum);

        BaseModel rs = new BaseModel(complaint);

        return ResponseEntity.ok().body(rs);
    }

    /**
     * QA 삭제하기
     * @param co_pk_conum
     * @return
     */
    @DeleteMapping("/qa/delete/{co_pk_conum}")
    public ResponseEntity<? extends BaseModel> qaDelete(@PathVariable(name = "co_pk_conum") int co_pk_conum){

        boolean result = svcenterServcie.qaDelete(co_pk_conum);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }



}
