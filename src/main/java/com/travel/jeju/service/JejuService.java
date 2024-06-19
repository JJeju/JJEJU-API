package com.travel.jeju.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.travel.jeju.dto.BimgDto;
import com.travel.jeju.dto.BreplyDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.EventDto;
import com.travel.jeju.dto.JejusimgDto;
import com.travel.jeju.dto.JejuspotDto;
import com.travel.jeju.dto.JejuspotreviewDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.dto.jejuplanDto;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.JejuplanMapper;
import com.travel.jeju.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JejuService {

    private JejuplanMapper jejuplanMapper;

	private final JwtTokenProvider jwtTokenProvider;

    private final int listCnt = 6;

    private String listName = "";

	public Map<String, Object> eventInfoList(Integer e_pk_enum) {
		log.info("eventInfoList()");
		Map<String, Object> result = new HashMap<>();
		
		EventDto edto = jejuplanMapper.eventList(e_pk_enum);
		
		result.put("edto", edto);
		
		return result;
	}


	public Map<String, Object> eventList(ListDto list) {
        log.info("eventList()");

		int listCnt = 4;
		
		if (list.getPageNum() == 0) list.setPageNum(1);

		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<EventDto> edto = jejuplanMapper.eventListselect(list);

		Map<String, Object> result = new HashMap<>();

		result.put("edto", edto);

		list.setPageNum(num);
		listName = "JejuEvent_List?";

		String pageHtml = geteventPaging(list);
		result.put("paging", pageHtml);

		return result;
    }

	public void reviewUpdate(MultipartHttpServletRequest multi) throws IllegalStateException, IOException {

		ClassPathResource resource = new ClassPathResource("/upload/");
		String path = resource.getPath();
		
		String filecheck = (String)multi.getParameter("FileCheck");
		
		File upFolder = new File(path);

		if(!upFolder.isDirectory()) upFolder.mkdir();

		String pknum = multi.getParameter("sv_pk_num");
		JejuspotreviewDto reDto = jejuplanMapper.replynumselect(pknum);

		deleteFile(reDto.getSv_img());

		JejuspotreviewDto sDto = new JejuspotreviewDto();
		sDto.setSv_pk_num(pknum);   
		sDto.setSv_star(multi.getParameter("sv_star"));
		sDto.setSv_fk_id(multi.getParameter("sv_fk_id"));
		sDto.setSv_fk_num(multi.getParameter("sv_fk_num"));
		//sDto.setSv_fk_nickname(mdto.getM_nickname());
		sDto.setSv_contents(multi.getParameter("sv_contents"));
		// sDto.setSv_date(multi.getParameter("sv_date"));
		
		Iterator<String> files = multi.getFileNames();
		
		if(filecheck.equals("1")) {
			while(files.hasNext()) {
				String fn = files.next();
				
				//multiple 선택 파일 처리 -> 파일 목록 가져오기
				List<MultipartFile> fList = multi.getFiles(fn);
				
				//각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
				for(int i = 0; i < fList.size(); i++) {
					MultipartFile mf = fList.get(i);
					
					//파일명 추출
					String oriname = mf.getOriginalFilename();
					
					//변경할 이름 생성
					String sysname = System.currentTimeMillis()
						+ oriname.substring(oriname.lastIndexOf("."));
					
					//upload 폴더 파일 저장
					File ff = new File(path + sysname);
					sDto.setSv_img(sysname);
					mf.transferTo(ff);
				}
			}
		}
		
		jejuplanMapper.spotreviewUpdate(sDto);
	}

	public Map<String, JejuspotreviewDto> replynumselect(String renum) {
        Map<String, JejuspotreviewDto> rmap = new HashMap<String, JejuspotreviewDto>();
		
		JejuspotreviewDto reDto = jejuplanMapper.replynumselect(renum);
		rmap.put("reply", reDto);
		
		return rmap;
    }

	public String breDelete(String sv_pk_num) {

		String msg = null;

		try {
			jejuplanMapper.breDelete(sv_pk_num);
			msg = "yes";

			
		} catch (Exception e) {
			log.error("",e);
			msg = "no";
		}

		return msg;
    }

	public void reviewInsert(MultipartHttpServletRequest multi) throws IllegalStateException, IOException {
		
		ClassPathResource resource = new ClassPathResource("/upload/");
		String path = resource.getPath();
		
		JejuspotreviewDto sDto = new JejuspotreviewDto();
		// JejuspotDto jsdto = new JejuspotDto();
		
		String filecheck = (String)multi.getParameter("FileCheck");
		
		File upFolder = new File(path);

		if(!upFolder.isDirectory()) upFolder.mkdir(); 

		sDto.setSv_star(multi.getParameter("sv_star"));
		sDto.setSv_fk_id(multi.getParameter("sv_fk_id"));
		sDto.setSv_fk_num(multi.getParameter("sv_fk_num"));
		//sDto.setSv_fk_nickname(mdto.getM_nickname());
		sDto.setSv_contents(multi.getParameter("sv_contents"));
		// sDto.setSv_date(multi.getParameter("sv_date"));
		

		Iterator<String> files = multi.getFileNames();
		
		if(filecheck.equals("1")) {
			while(files.hasNext()) {
				String fn = files.next();
				
				//multiple 선택 파일 처리 -> 파일 목록 가져오기
				List<MultipartFile> fList = multi.getFiles(fn);
				
				//각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
				for(int i = 0; i < fList.size(); i++) {
					MultipartFile mf = fList.get(i);
					
					//파일명 추출
					String oriname = mf.getOriginalFilename();
					
					//변경할 이름 생성
					String sysname = System.currentTimeMillis()
						+ oriname.substring(oriname.lastIndexOf("."));
					
					//upload 폴더 파일 저장
					File ff = new File(path + sysname);
					sDto.setSv_img(sysname);
					mf.transferTo(ff);
				}
			}
		}
		
		jejuplanMapper.spotreviewInsert(sDto);
	}

	public Map<String, Object> spotinfo(int jnum) {
		Map<String, Object> result = new HashMap<>();

		List<JejusimgDto> siList = jejuplanMapper.spotimgList(jnum);
		List<JejuspotDto> sList = jejuplanMapper.spotInfo(jnum);
		List<JejuspotreviewDto> rList = jejuplanMapper.spotreview(jnum);
		List<JejusimgDto> siList2 = jejuplanMapper.spotimgList2(jnum);
	    List<JejusimgDto> siList3 = jejuplanMapper.spotimgList3(jnum);
	    List<JejusimgDto> siList4 = jejuplanMapper.spotimgList4(jnum);
		List<JejuspotreviewDto> riList = jejuplanMapper.spotreview(jnum);
		
	    result.put("siList2", siList2);
	    result.put("siList3", siList3);
	    result.put("siList4", siList4);
	    result.put("riList", riList);

		result.put("siList", siList);
		result.put("sList", sList);
		result.put("rList", rList);
		result.put("jnum", jnum);
		//mv.addObject("si2List", si2List);
		return result;
	}

	public Map<String, Object> getspotlist(ListDto list) {
        Map<String, Object> result = new HashMap<>();
		
		if(list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<JejuspotDto> spList = jejuplanMapper.spotList(list);// 리턴된값은 pList로 다시 넘겨 받는다

		result.put("spList", spList);

		list.setPageNum(num);
		
		listName = "jejutourList?";
		String pageHtml = getPaging2(list);
		result.put("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)
		// session.setAttribute("pageNum", list.getPageNum()); // 해당 페이지 37번째줄 pageNum과 같은 이름으로 작성!

		// if (list.getColname() != null) {
		// 	session.setAttribute("list", list);
		// }

	    return result;
    }

	public Map<String, List<BreplyDto>> replyInsert(BreplyDto reply) {
		Map<String, List<BreplyDto>> rmap = null;

		try {
			// 댓글 삽입
			jejuplanMapper.replyInsert(reply);
			// 댓글 목록 불러오기 및 rmap에 추가(getR_bnum() 어떤글의 댓글이 들어오는알아야하기때문에? 리플라이안에 꼭 필요함)
			List<BreplyDto> rList = jejuplanMapper.replySelect(reply.getBr_fk_num());

			rmap = new HashMap<String, List<BreplyDto>>();
			rmap.put("rList", rList);
		} catch (Exception e) {
			log.error("",e);
			rmap = null;
		}

		return rmap;
	}

	public Map<String, Object> jejuplanInfo(int jnum) {
		

		List<jejuplanDto> jList = new ArrayList<>();
		List<BimgDto> bList = new ArrayList<>();
		List<BreplyDto> rList = jejuplanMapper.replySelect(jnum);

		jList = jejuplanMapper.jejuplanInfo(jnum);
		bList= jejuplanMapper.jejuplanImg(jnum);

		Map<String, Object> result = new HashMap<>();

		result.put("jList", jList);
		result.put("bList", bList);
		result.put("rList", rList);
		result.put("jnum", jnum);

		return result;
	}

	@Transactional
	public boolean jejuplanInsert(MultipartHttpServletRequest multi) {
		
		CompanyDto cdto = new CompanyDto();
		MemberDto mdto = new MemberDto();
		TravelrouteDto tdto = new TravelrouteDto();
		
		String sysname = null;

		String token = jwtTokenProvider.getHeaderToken();
		String m_username = jwtTokenProvider.getSubject(token);
		
		jejuplanDto dto = new jejuplanDto();
		dto.setB_fk_id(m_username);
		dto.setB_pk_num(multi.getParameter("b_pk_num"));
		dto.setB_fk_tnum(String.valueOf(tdto.getTr_pk_num()));
		dto.setB_img(multi.getParameter("b_img"));
		dto.setB_title(tdto.getTr_title());
		dto.setB_cost(multi.getParameter("b_cost"));
		dto.setB_contents(multi.getParameter("b_contents"));
		//dto.setB_date(multi.getParameter("b_date"));
		
		BimgDto bdto = new BimgDto();
		bdto.setBi_fk_num(multi.getParameter("bi_fk_num"));
		bdto.setBi_pk_num(dto.getB_pk_num());
		bdto.setBi_oriname(multi.getParameter("bi_oriname"));
		bdto.setBi_sysname(multi.getParameter("bi_sysname"));
		try {
			sysname = fileupload(multi);
		} catch (Exception e) {
			log.error("",e);
		}
		dto.setB_img(sysname);
		
		Boolean result = null;

		try {
			jejuplanMapper.JejuplanInsert(dto);
			result = true;
			
		} catch (Exception e) {
			log.error("",e);
			result = false;
		}

		jejuplanMapper.BimgInsert(bdto);
		
		return result;
	}

    public Map<String, Object> planList(ListDto list) {

		Map<String, Object> result = new HashMap<>();
				
		if(list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<jejuplanDto> pList = jejuplanMapper.planList(list);// 리턴된값은 pList로 다시 넘겨 받는다

		result.put("pList", pList);

		list.setPageNum(num);

		return result;
	}

	public void deleteFile(String filename) {
		
		// 파일의 경로 + 파일명
		ClassPathResource resource = new ClassPathResource("/upload/");
		String path = resource.getPath();
		
		File deleteFile = new File(path);
		
		System.out.println(path);
	
		// 파일이 존재하는지 체크 존재할경우 true, 존재하지않을경우 false
		if(deleteFile.exists()) {
			
			// 파일을 삭제합니다.
			deleteFile.delete(); 
			
			System.out.println("파일을 삭제하였습니다.");
			
		} else {
			System.out.println("파일이 존재하지 않습니다.");
		}
	}

	private String geteventPaging(ListDto list) {
		String pageHtml = null;

		// 전체 글개수 구하기
		int maxNum = jejuplanMapper.eventCntList(list);
		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), list.getListCnt(), pageCnt, listName);

		pageHtml = paging.makePaging();

		return pageHtml;
	}

	private String fileupload(MultipartHttpServletRequest multi) throws IllegalStateException, IOException {
		String sysname = null;

		Iterator<String> files = multi.getFileNames();

		while (files.hasNext()) {
			String fn = files.next();

			// multiple 선택 파일 처리 -> 파일 목록 가져오기
			List<MultipartFile> fList = multi.getFiles(fn);

			// 각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
			for (int i = 0; i < fList.size(); i++) {
				MultipartFile mf = fList.get(i);

				// 파일명 추출
				String oriname = mf.getOriginalFilename();

				// 변경할 이름 생성
				sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

				// upload 폴더 파일 저장
				File ff = new File(sysname);
				mf.transferTo(ff);
			}
		}
		return sysname;
	}

    private String getPaging(ListDto list) {
		String pageHtml = null;

		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;
		int maxNum = jejuplanMapper.jejuplancnt(list);
		// 검색 용 컬럼명과 검색어를 추가
		if (list.getColname() != null) {
			listName += "colname=" + list.getColname() + "&keywoard=" + list.getKeyword() + "&";
		}

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName); // 클래스 임포트!!

		pageHtml = paging.makePaging();

		return pageHtml; // 여기서 리턴값 받은것이 해당 클래스 45 번째줄로 간다!!
	}

	private String getPaging2(ListDto list) {
		String pageHtml = null;

		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;
		int maxNum = jejuplanMapper.spotcnt(list);
		// 검색 용 컬럼명과 검색어를 추가
		if (list.getColname() != null) {
			listName += "colname=" + list.getColname() + "&keywoard=" + list.getKeyword() + "&";
		}

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName); // 클래스 임포트!!

		pageHtml = paging.makePaging();

		return pageHtml; // 여기서 리턴값 받은것이 해당 클래스 45 번째줄로 간다!!

	}

	
}
