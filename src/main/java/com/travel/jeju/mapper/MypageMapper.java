package com.travel.jeju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.BimgDto;
import com.travel.jeju.dto.BlogDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.EventDto2;
import com.travel.jeju.dto.FavoritesDto;
import com.travel.jeju.dto.IamBasketDto;
import com.travel.jeju.dto.LastBlogDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.dto.NowTripDto;
import com.travel.jeju.dto.PastTripBlogDto;
import com.travel.jeju.dto.RevDto;
import com.travel.jeju.dto.ReviewDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.model.mypage.blog.select.MypageBlogParam;
import com.travel.jeju.model.mypage.deal.MypageDealParam;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteParam;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteVO;
import com.travel.jeju.model.mypage.review.MypageReviewParam;
import com.travel.jeju.model.mypage.travel.MypageTravelParam;

@Mapper
public interface MypageMapper {

	//마이페이지에 최근거래 목록 가져오기
	public List<IamBasketDto> selectMypagePayment(String username);//목록을 묶어서 가져오겠다.

	//마이페이지에 지난 여행 목록 가져오기
	public List<PastTripBlogDto> selectMypagePastTripBlog(String username);
	
	//게시글 목록 가져오기
	public List<IamBasketDto> selectDealList(MypageDealParam param);//목록을 묶어서 가져오겠다.

	public List<MypageFavoriteVO> selectFavorite(MypageFavoriteParam param);

	//Mypage_Plan 페이지에 게시판 리스트 출력
	public List<BlogDto> selectMypageBlog(MypageBlogParam param);

	public boolean updateBlog(BlogDto blogdto);

	public ReviewDto selectReviewById(Integer r_num);

	public boolean deleteBimgByBnum(Integer bnum);

	public boolean deleteBreplyByBnum(Integer value);

	public boolean deleteBlogById(Integer value);

	//Review_Lodgment 에 숙박 리뷰 출력하기
	public List<RevDto> selectMypageReview(MypageReviewParam param);//목록을 묶어서 가져온다.

	//Mypage_plan에 해당정보와 일치한 상세내용 팝업창에 출력
	public BlogDto selectMypageBlogById(Integer b_pk_num);

	//NowTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보를 페이지에 출력
	// public TravelrouteDto nowtripinfo(Integer tr_pk_num);
	public TravelrouteDto selectTravelById(Integer tr_pk_num);

	public List<BasketDto> selectBasketByTnum(Integer bk_fk_tnum);

	public boolean deleteBasketByTnum(Integer tr_pk_num);

	//NowTrip 페이지에 여행리스트 삭제 버튼 클릭시 발동
	public boolean deleteTravelById(Integer tr_pk_num);

	//NowTrip 페이지에 여행리스트 여행완료 버튼 클릭시 발동
	public boolean updateTravleComplate(TravelrouteDto travel);

	//NowTrip 페이지에 여행 리스트 출력
	public List<TravelrouteDto> selectTravelByUsername(MypageTravelParam param);

	public FavoritesDto selectFavoriteById(Integer fa_pk_num);

	public void deleteFavoriteById(Integer fa_pk_num);

	public boolean createBlog(BlogDto param);






	
	//전체 글 갯수 구하기
	public int bcntSelect(IamBasketDto list);
	
	

	//Review_Lodgment 에 해당 리스트 삭제
	public boolean delreview(Integer rv_pk_num);

	//Review_Activiry 에 레저 리뷰 출력하기
	public List<RevDto> ActiviryListSelect(RevDto rlist);

	//Review_Food 에 식당 리뷰 출력하기
	public List<RevDto> FoodListSelect(RevDto rlist);

	
	//NowTrip 페이지에 여행 리스트 출력
	public List<TravelrouteDto> pastTripListSelect(String tr_fk_id);
	
	//달력 테스트 이벤트
	public List<EventDto2> eListSelect(EventDto2 edto);
	
	
	
	//NowTrip 페이지실행시 달력에 정보 최신업데이트를위한 insert 작업.
//	public void cListInsert(NowTripDto ndto);
	
	
	
	
	
	

	
	
	//Mypage_Plan 페이지 게시판 리스트 개수 구하기
	public int PlancntSelect(ListDto list);
	
	
	
	//Mypage_plan 페이지 팝업창에 삭제버튼 실행시 삭제
	public void plandel(Integer b_pk_num);

	

	//PastTrip 페이지에 여행완료된 리스트 출력
	public List<NowTripDto> pastTripSelect(NowTripDto ndto);

	//PastTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보 페이지 출력
	public TravelrouteDto pasttripinfo(Integer tr_pk_num);
	
	
	
	public BlogDto bnumblogselect(Integer tr_pk_num);
	//PastTrip 페이지에 삭제버튼 클릭시 발동
	public void pasttripDel(Integer tr_pk_num);
	
	public void pasttripbasketDel(Integer tr_pk_num);
	
	
	
	public void pasttripblogDel(Integer tr_pk_num);

	//PastTrip 페이지에 달력출력
	public List<EventDto2> pasteListSelect(EventDto2 edto);

	
	/*
	//Plan_Update 페이지에 수정 버튼 클릭시 발동
	public void blogUpdate(BlogDto bdto);

	//Plan_Update 페이지에 수정 버튼 클릭시 발동
	public void bimgUpdate(BimgDto bidto);
	 */
	
	
	//Plan_Update 페이지에 수정버튼 클릭시 발동
	public void blogUpdate(Integer bnum);
	
	//Plan_Update 페이지에 수정버튼 클릭시 발동
	public void bimgUpdate(BimgDto bidto);

	

	
	
	
	
	
	
	
	

	public TravelrouteDto travelSelect(int trnum);



}
