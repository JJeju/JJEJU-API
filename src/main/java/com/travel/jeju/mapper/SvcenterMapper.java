package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.ComplaintDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.dto.NoticeDto;
import com.travel.jeju.model.common.PageModel;
import com.travel.jeju.model.svcenter.qa.QaMyListParam;

@Mapper
public interface SvcenterMapper {
	// 공지사항 List 구하기
	public List<NoticeDto> selectNotice(PageModel param);

	// n_pk_num으로 Notice 정보 select
	public NoticeDto selectNoticeById(Integer n_pk_num);

	// Notice 테이블 view 1증가 업데이트
	public void updateNoticeView(Integer n_pk_num);

	// ComplaintDto select
	public List<ComplaintDto> selectComplaint(PageModel param);

	// Complaint insert
	public boolean insertComplaint(ComplaintDto plaint);

	public boolean deleteComplaintById(Integer co_pk_conum);

	public boolean deleteComplaintByTitle(String title);

	// session으로 얻은 co_fk_id로 select 
	public List<ComplaintDto> selectComplaintByUsername(QaMyListParam param);

	// co_pk_num으로 dto select
	public ComplaintDto selectComplaintById(Integer co_pk_conum);






	// 공지사항 table 정보 갯수 숫자 구하기
	public int noticeCntSelect(ListDto list);
	
	
	
	
	
	
	

	
	// co_pk_num으로 delete
	public void qaDelect(String co_pk_conum);

}
