package com.travel.jeju.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.travel.jeju.dto.AactivityDto;
import com.travel.jeju.dto.AimgDto;
import com.travel.jeju.dto.AoptionDto;
import com.travel.jeju.dto.CimgDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.dto.LoptionDto;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.ReviewDto;
import com.travel.jeju.dto.RoomDto;
import com.travel.jeju.dto.RoomImgDto;
import com.travel.jeju.model.main.business.BusinessItemModel;
import com.travel.jeju.model.main.business.BusinessPlaceModel;

@Mapper
public interface CompanyMapper {

   // Room 테이블 PK 값으로 개별 select
   public RoomDto selectRoomById(Integer param);

   // Aactivity 테이블 PK값으로 개별 select
   public AactivityDto selectAactivityById(String num);

   // Company테이블 Insert
   public void CompanyInsert(CompanyDto dto);
   // Company테이블 c_pk_cnum으로 c_img컬럼 업데이트
   public void CompanyC_imgU(Map<String, String> smap);
   // Company테이블 m_username로 전체 select
   public List<CompanyDto> getList(String m_username);
   // Company테이블 m_username값의 count select
   public int getListCount(String m_username);
   // c_pk_cnum 으로 select
   public CompanyDto selectCompanyById(Integer param);

   public BusinessPlaceModel selectCompanyByIdWithFileData(Integer param);

   public List<BusinessItemModel> selectProductByCompanyPkAndWithFileData(Integer cnum);

   // ListDto를 활용한 Company테이블 select
   public List<CompanyDto> searchCompany(ListDto dto);
   // CompanyDto c_condition 반대값으로 변경
   public void updateCondition(CompanyDto dto);
   // c_pk_cnum 으로 delete
   public void deleteCompany(String cnum);
   // Member 테이블 사업자 -> 개인으로 변경
   public void solMemChange(MemberDto dto);
   // c_pk_cnum 중복 확인 , 있을시 1 없을시 0
   public int cnumDeduplication(String cnum);
   // company테이블 CompanyDto로 update
   public void companyUpdate(CompanyDto cdto);
   // company테이블 c_fk_id 값에 맞는 정보 c_check = 0으로 update
   public void companyOff(String id);
   
   // Cimg 테이블 insert
   public void CimgInsert(Map<String, String> cmap);
   // Cimg 테이블 c_pk_cnum으로 select
   public List<CimgDto> getCimgList(Integer cnum);
   // Cimg 테이블 ci_sysname으로 delete
   public void delCimg(String sysname);
   // Cimg 테이블 c_pk_cnum값으로 delete
   public void deleteCimg(String cnum);
   
   
   // Company insert후 바로 option insert
   public void loptionInsert(String c_pk_cnum);
   // Company insert후 바로 option insert   
   public void aoptionInsert(String c_pk_cnum);
   // Company insert후 바로 option Delete   
   public void loptionDelete(String c_pk_cnum);
   // Company insert후 바로 option Delete   
   public void aoptionDelete(String c_pk_cnum);
   // Loption Update
   public void loptionUpdate(LoptionDto lpdto);
   // Aoption Update
   public void aoptionUpdate(AoptionDto apdto);

   // Loption cnum으로 select
   public LoptionDto selectLoptionByCnum(Integer cnum);

   // Aoption cnum으로 select
   public AoptionDto selectAoptionByCnum(Integer cnum);
   
   // room 테이블 Insert
   public void roomInsert(RoomDto dto);
   // room 테이블 사진 update
   public void roomImgUpdate(Map<String, String> fmap);
   // Room 테이블 c_pk_cnum으로 select
   public List<RoomDto> selectRoomByCnum(Integer data);
   
   // Room 테이블 전체 update
   public void roomUpdate(RoomDto dto);
   // Room 테이블 PK 값으로 개별 delete
   public void delPK_Room(String pknum);
   
   
   // rImg 테이블 Insert
   public void rImgInsert(Map<String, String> fmap);
   // rImg 테이블 rnum으로 select
   public List<RoomImgDto> getRimgList(String ri_fk_num);
   // rImg 테이블 sysname값으로 사진 하나 delete
   public void solDelrImg(String sysname);
   // rImg 테이블 ri_fk_num 값으로 사진 delete
   public void del_fk_delRImg(String ri_fk_num);
   
   
   
   // Aactivity 테이블 select
   public List<AactivityDto> selectAactivityByCnum(Integer data);
   
   // Aactivity 테이블 Insert
   public void AactivityInsert(AactivityDto dto);
   // Aactivity 테이블 사진 update
   public void aactivityImgUpdate(Map<String, String> fmap);
   // Aactivity 테이블 전체 update
   public void aactivityUpdate(AactivityDto adto);
   // Room 테이블 PK 값으로 개별 delete
   public void delPK_Aactivity(String pknum);
   
   
   // aImg 테이블 Insert
   public void aImgInsert(Map<String, String> fmap);
   // aImg 테이블 atnum으로 select
   public List<AimgDto> getAimgList(String ai_fk_num);
   // aImg 테이블 sysname값으로 사진 하나 delete
   public void solDelaImg(String sysname);
   // aImg 테이블 ai_fk_num 값으로 사진 delete
   public void del_fk_delAImg (String ai_fk_num);
   
   // c_pk_cnum으로 review 테이블 구하기
   public List<ReviewDto> selectReviewByCnum(Integer cnum);
   // review 테이블 대답 업데이트
   public void reviewAnswer(ReviewDto dto);
   // review 테이블 c_pk_cnum 값으로 delete
   public void reviewDelete(String cnum);
   // review 테이블 m_username 값으로 delete
   public void reviewMemberDelete(String id);
   
   
   
   
   
   
   

   
}