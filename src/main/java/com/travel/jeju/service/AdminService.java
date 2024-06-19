package com.travel.jeju.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.mapper.AdminMapper;
import com.travel.jeju.model.travel.plan.TravelPlanInsertRQ;
import com.travel.jeju.util.PagingUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

   private final AdminMapper adminMapper;

   private final int listCnt = 5;//페이지 당 출력할 게시글 개수

   private String listName = "";
   
   private int maxNum = 0;

   public Map<String,Object> selectCompanylist(ListDto list) {

      Map<String, Object> result = new HashMap<>();
      
      if(list.getPageNum() == 0) list.setPageNum(1);
      
      int num = list.getPageNum();
      list.setPageNum((num - 1) * listCnt);
      list.setListCnt(listCnt);
      
      List<CompanyDto> cList = adminMapper.selectClist(list);
      
      result.put("cList", cList);
      
      
      CompanyDto dto = new CompanyDto();
      
      for (int i = 0; i < cList.size(); i++) {
         switch (cList.get(i).getC_check()) {
         case "1":
               dto = cList.get(i);
               dto.setC_check("승인");
               cList.set(i, dto);
               break;
         case "0":
               dto = cList.get(i);
               dto.setC_check("미승인");
               cList.set(i, dto);
               break;
         }
      }
      
      for (int i = 0; i < cList.size(); i++) {
         switch (cList.get(i).getC_condition()) {
         case "1":
               dto = cList.get(i);
               dto.setC_condition("영업중");
               cList.set(i, dto);
               break;
         case "0":
               dto = cList.get(i);
               dto.setC_condition("영업준비중");
               cList.set(i, dto);
               break;
         }
      }
      
      //페이징 처리
      list.setPageNum(num);
      listName = "Admin_Company_List?";
      maxNum = adminMapper.selectCntClist(list);
      String pageHtml = getPaging(list);
      result.put("paging", pageHtml);
      
      //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
      //돌아 갈 때 사용할 페이지 번호를 저장)
      
      return result;
   }

   private String getPaging(ListDto list) {
      String pageHtml = null;
      
      //전체 글개수 구하기
      //한 페이지 당 보여질 페이지 번호의 개수
      int pageCnt = 5;
      
      
      //검색 용 컬럼명과 검색어를 추가
      if(list.getKeyword() != null) {
         listName += "colname="+ list.getColname()
            + "&keyword=" + list.getKeyword() + "&";
      } if(list.getCo() != null) {
         listName += "co="+ list.getCo() + "&";
      } if(list.getPr() != null) {
         listName += "pr="+ list.getPr() + "&";
      } if(list.getBl() != null) {
         listName += "bl="+ list.getBl() + "&";
      } if(list.getSage() != null) {
         listName += "sage="+ list.getSage() + "&";
      } if(list.getEage() != null) {
         listName += "eage="+ list.getEage() + "&";
      } if(list.getSelgender() != null) {
         listName += "selgender=" + list.getSelgender() + "&";
      } if(list.getAt() != null) {
         listName += "at=" + list.getAt() + "&";
      } if(list.getFo() != null) {
         listName += "fo=" + list.getFo() + "&";
      } if(list.getLod() != null) {
         listName += "lod=" + list.getLod() + "&";
      } if(list.getLod() != null) {
         listName += "condition=" + list.getConditionOn() + "&";
      } if(list.getLod() != null) {
         listName += "condition=" + list.getConditionOff() + "&";
      } if(list.getLod() != null) {
         listName += "check=" + list.getCheckOn() + "&";
      } if(list.getLod() != null) {
         listName += "check=" + list.getCheckOff() + "&";
      }   
      
      PagingUtil paging = new PagingUtil(maxNum, 
            list.getPageNum(),
            listCnt, pageCnt, listName);
      
      pageHtml = paging.makePaging();
      
      return pageHtml;
   }
}
