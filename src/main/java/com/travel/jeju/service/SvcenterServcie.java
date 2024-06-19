package com.travel.jeju.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.dto.ComplaintDto;
import com.travel.jeju.dto.NoticeDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.SvcenterMapper;
import com.travel.jeju.model.common.PageModel;
import com.travel.jeju.model.svcenter.notice.NoticeRQ;
import com.travel.jeju.model.svcenter.notice.NoticeRS;
import com.travel.jeju.model.svcenter.qa.QaInsertRQ;
import com.travel.jeju.model.svcenter.qa.QaMyListParam;
import com.travel.jeju.model.svcenter.qa.QaMyListRQ;
import com.travel.jeju.model.svcenter.qa.QaMyListRS;
import com.travel.jeju.model.svcenter.qa.QaRQ;
import com.travel.jeju.model.svcenter.qa.QaRS;
import com.travel.jeju.util.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SvcenterServcie {

    private final SvcenterMapper svcenterMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final int listCnt = 5;

    @Transactional
    public boolean qaDelete(int co_pk_conum) {

        ComplaintDto complaint = svcenterMapper.selectComplaintById(co_pk_conum);

        if(complaint == null) return true;

        String fk_id = complaint.getCo_fk_id();
        String username = Principal.getUsername();

        if(!(fk_id.equals(username))) throw new AppException(ExceptionCode.NOT_PERMISSION);

        boolean result = true;

        try {
            svcenterMapper.deleteComplaintById(co_pk_conum);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }

        return result;
    }

    public ComplaintDto qaDetail(int co_pk_conum) {

        return svcenterMapper.selectComplaintById(co_pk_conum);
    }

    public QaMyListRS qaMyList(QaMyListRQ rq) {

        String username = Principal.getUsername();

        int pageNum = rq.getPageNum() == 0 ? 1 : rq.getPageNum();

        QaMyListParam param = new QaMyListParam();
        param.setListCnt(this.listCnt);
        param.setPageNum((pageNum - 1) * this.listCnt);
        param.setUsername(username);

        List<ComplaintDto> complaint = svcenterMapper.selectComplaintByUsername(param);

        QaMyListRS result = new QaMyListRS();
        result.setComplaint(complaint);

        return result;
    }

    @Transactional
    public boolean qaInsert(QaInsertRQ rq) {

        String username = Principal.getUsername();

        ComplaintDto param = new ComplaintDto();
        param.setCo_title(rq.getCo_title());
        param.setCo_contents(rq.getCo_contents());
        param.setCo_fk_id(username);

        boolean result = true;

        try {
            svcenterMapper.insertComplaint(param);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }        

        return result;
    }

    public QaRS qa(QaRQ rq) {

        int pageNum = rq.getPageNum() == 0 ? 1 : rq.getPageNum();

        PageModel param = new PageModel();
        param.setListCnt(this.listCnt);
        param.setPageNum((pageNum - 1) * this.listCnt);

        List<ComplaintDto> complaint = svcenterMapper.selectComplaint(param);
        
        QaRS result = new QaRS();
        result.setComplaint(complaint);

        return result;
    }

    public NoticeDto noticeDetail(int n_pk_num) {
        
        svcenterMapper.updateNoticeView(n_pk_num);

        return svcenterMapper.selectNoticeById(n_pk_num);
    }
    
    public NoticeRS notice(NoticeRQ rq) {
        int pageNum = rq.getPageNum() == 0 ? 1 : rq.getPageNum();

        PageModel param = new PageModel();
        param.setListCnt(this.listCnt);
        param.setPageNum((pageNum - 1) * this.listCnt);
        
        List<NoticeDto> notice = svcenterMapper.selectNotice(param);

        NoticeRS result = new NoticeRS();
        result.setNotice(notice);

        return result;
    }

    
}
