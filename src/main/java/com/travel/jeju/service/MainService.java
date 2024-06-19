package com.travel.jeju.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.travel.jeju.constant.FileDescConstant;
import com.travel.jeju.dto.AactivityDto;
import com.travel.jeju.dto.AimgDto;
import com.travel.jeju.dto.AoptionDto;
import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.BlogDto;
import com.travel.jeju.dto.CimgDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.ComplaintDto;
import com.travel.jeju.dto.EventDto;
import com.travel.jeju.dto.FavoritesDto;
import com.travel.jeju.dto.LoptionDto;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.NoticeDto;
import com.travel.jeju.dto.ReviewDto;
import com.travel.jeju.dto.RoomDto;
import com.travel.jeju.dto.RoomImgDto;
import com.travel.jeju.dto.SpotDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.CompanyMapper;
import com.travel.jeju.mapper.FileMapper;
import com.travel.jeju.mapper.MainMapper;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.basket.InsertBasketRQ;
import com.travel.jeju.model.basket.InsertBasketReserveRQ;
import com.travel.jeju.model.common.FileModel;
import com.travel.jeju.model.main.business.BusinessFileModel;
import com.travel.jeju.model.main.business.BusinessItemModel;
import com.travel.jeju.model.main.business.BusinessPlaceModel;
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
import com.travel.jeju.util.FileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final MemberMapper memberMapper;
    
    private final MainMapper mainMapper;

    private final CompanyMapper companyMapper;

    private final FileMapper fileMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final FileUtil fileUtil;

    /**
     * 공개여부가 TRUE인 값들만 랜덤으로 후기글 6개 가져오는 기능
     * @return
     */
    public SelectPublicBlogRS getPublicBlog() {

        List<BlogDto> publicBlogList = mainMapper.selectTop6RandomPublicBlog();
        
        SelectPublicBlogRS result = new SelectPublicBlogRS();
        result.setPublicBlogList(publicBlogList);

        return result;
    }

    @Transactional
    public boolean favoritInsert(InsertFavoriteRQ rq) {
        FavoritesDto dto = new FavoritesDto();
        dto.setFa_fk_id(rq.getFa_fk_id());
        dto.setFa_fk_cnum(rq.getFa_fk_cnum());

        boolean result = true;
		try {
			mainMapper.favoritesInsert(dto);

		} catch (Exception e) {
			log.error("",e);
            result = false;
		}
	
		return result;
    }

    @Transactional
    public boolean reviewInsert(InsertReviewRQ rq) {
        log.info("MainService - reviewInsert()");
        
        String token = jwtTokenProvider.getHeaderToken();
        String username = jwtTokenProvider.getSubject(token);

        MemberDto mdto = memberMapper.memberSelect(username);

        ReviewDto rdto = new ReviewDto();
        
        rdto.setRv_fk_cnum(rq.getFv_fk_cnum());
        rdto.setRv_contents(rq.getRv_contents());
        rdto.setRv_star(rq.getRv_start());
        rdto.setRv_fk_id(mdto.getM_username());

        log.info("별점 => {}", rdto.getRv_star());

        boolean result = true;
        
        try {
            mainMapper.reviewInsert(rdto);
            if(rq.getR_img() != null) {
                log.info("파일 체크 : " + rdto.getFilecheck());
                // cfileupload(rdto.getRv_pk_num(), rq.getR_img());

                List<MultipartFile> fileParam = Arrays.asList(rq.getR_img());
                int fileGroupNo = fileUtil.saveFile(fileParam, FileDescConstant.REVIEW.getValue());

                rdto.setFile_group_no(fileGroupNo);

                mainMapper.updateReviewImg(rdto);
            }
        } catch (Exception e) {
            log.error("",e);
            
            throw new AppException(ExceptionCode.FILE_NOT_FOUND);
        }
        
        return result;
    }

    public Map<String, Object> optionCheck(OptionCheckRQ rq) {
        log.info("MainService - optionCheck()");

        Map<String, Object> omap = new HashMap<String, Object>();
        List<CompanyDto> cList = new ArrayList<CompanyDto>();
        List<LoptionDto> lList = new ArrayList<LoptionDto>();
        List<AoptionDto> aoList = new ArrayList<AoptionDto>();
        
        String cate = rq.getCategory();
        List<String> optionList = rq.getOptionList();


        switch(cate) {
        case "숙박" :
            LoptionDto lodto = loptionGet(optionList);
            
            lList = mainMapper.loptionCheck(lodto);
            
            for(int i = 0; i<lList.size(); i++) {
                CompanyDto cdto = companyMapper.selectCompanyById(lList.get(i).getL_fk_cnum());
                cList.add(cdto);
            }
            
            break;
        case "레저":
            AoptionDto aodto = aoptionGet(optionList);
            
            aoList = mainMapper.aoptionCheck(aodto);
            
            for(int i = 0; i<aoList.size(); i++) {
                CompanyDto cdto = companyMapper.selectCompanyById(aoList.get(i).getA_fk_cnum());
                cList.add(cdto);
            }
            
            break;
        }
        
        omap.put("cList", cList);
        log.info("리스트 숫자 : " + cList.size());
        
        return omap;
    }

   @Transactional
   public boolean payUpdate(UpdatePayRQ rq) {
      log.info("MainService - payUpdate()");
      
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      
      BasketDto bdto = new BasketDto();
      bdto.setBk_paych(rq.getBk_paych());
      bdto.setBk_pk_num(rq.getBk_pk_num());

      String nowTime = now.format(format);
      bdto.setBk_paytime(nowTime);
      
      log.info("현 시간 => {} ", bdto.getBk_paytime());

      boolean result = true;

      try {
         mainMapper.payUpdate(bdto);
      } catch (Exception e) {
         result = false;
         log.error("",e);
      }
      
      return result;
   }

   public SearchBusinessItemDetailRS item_infomation(Integer itemNumber) {

      BusinessItemModel item = mainMapper.selectProductById(itemNumber);

      if(item == null) throw new AppException(ExceptionCode.DATA_NO);

      List<BusinessFileModel> fileList = fileMapper.selectFileByFileGroupNoAndNotMain(item.getFile_group_no()).stream().map(BusinessFileModel::toRSModel).toList();

      SearchBusinessItemDetailRS result = new SearchBusinessItemDetailRS();
      result.setItemDetail(item);
      result.setItemFileList(fileList);

      return result;
   }

   public SearchBusinessItemRS businessItem(Integer c_pk_num) {

         BusinessPlaceModel company = companyMapper.selectCompanyByIdWithFileData(c_pk_num);
         List<FileModel> companyFileList = fileMapper.selectFileByFileGroupNoAndNotMain(company.getC_file_group_no());
         List<BusinessItemModel> companyItemList = companyMapper.selectProductByCompanyPkAndWithFileData(c_pk_num);
         
         SearchBusinessItemRS result = new SearchBusinessItemRS();
         result.setCompany(company);
         result.setCompanyFileList(companyFileList);
         result.setCompanyItemList(companyItemList);

         return result;
   }
    
    public SelectBusinessPlaceRS businessPlaceList(SelectBusinessPlaceRQ rq) {
        
        List<BusinessPlaceModel> companyRandomList = mainMapper.selectCompanyByCategory(rq.getCategory());

        SelectBusinessPlaceRS result = new SelectBusinessPlaceRS();
        result.setCompanyRandomList(companyRandomList);

        return result;
    }

    public HomeComplaintRS homeComplaint() {

        List<ComplaintDto> complaint = mainMapper.selectHomeComplaint();

        HomeComplaintRS result = new HomeComplaintRS();
        result.setComplaint(complaint);


        return result;
    }

    public HomeNoticeRS homeNotice() {

        List<NoticeDto> notice = mainMapper.selectHomeNotice();

        HomeNoticeRS result = new HomeNoticeRS();
        result.setNotice(notice);

        return result;
    }

    public HomeEventSpotRS homeEventAndSpot() {

        List<EventDto> event = mainMapper.selectHomeEvent();
        List<SpotDto> spot = mainMapper.selectHomeSpot();

        HomeEventSpotRS result = new HomeEventSpotRS();
        result.setEvent(event);
        result.setSpot(spot);

        return result;
    }

    public HomeBusinessRS homeBusiness() {

        List<CompanyDto> lodgment = mainMapper.selectHomeLodgment(); // 랜덤 숙박 업체 3곳
		List<CompanyDto> leisure = mainMapper.selectHomeLeisure(); // 랜덤 레저 업체 3곳
        List<CompanyDto> restaurant = mainMapper.selectHomeRestaurant();

        HomeBusinessRS result = new HomeBusinessRS();
        result.setLodgment(lodgment);
        result.setLeisure(leisure);
        result.setRestaurant(restaurant);

        return result;
    }
    
    public Map<String, Object> homeList() {

        Map<String, Object> result = new HashMap<>();

		List<ComplaintDto> pldto = mainMapper.selectHomeComplaint(); // 신고 사항 리스트 [5개 제한]
		List<NoticeDto> ndto = mainMapper.selectHomeNotice(); // 공지사항 리스트 [5개 제한]
		List<EventDto> edto = mainMapper.selectHomeEvent(); // 이벤트 리스트 [1개 제한]
		List<CompanyDto> cdto = mainMapper.selectHomeLodgment(); // 랜덤 숙박 업체 3곳
		List<CompanyDto> adto = mainMapper.selectHomeLeisure(); // 랜덤 레저 업체 3곳
		List<SpotDto> bigsdto = mainMapper.spotbigList(); // 광고할 관광지 1곳 [1개 제한]
		List<SpotDto> sdto = mainMapper.selectHomeSpot(); // 관광지 리스트 [2개 제한]
        
		List<CompanyDto> mdl = mainMapper.homemdcompanyList(); // 광고할 숙박업체 1곳 [가장 크게 있음]
		List<CompanyDto> mda = mainMapper.homemdactiveList(); // 광고할 레저업체 1곳 [가장 크게 있음]
	
		result.put("mdl", mdl);
		result.put("mda", mda);
		result.put("bigsdto", bigsdto);
		result.put("sdto", sdto);
		result.put("adto", adto);
		result.put("cdto", cdto);
		result.put("edto", edto);
		result.put("ndto", ndto);
		result.put("pldto", pldto);

        return result;
    }

    @Transactional
    private String cfileupload(int rv_pk_num, MultipartFile r_img) throws IllegalStateException, IOException {
        String sysname = null;

        ClassPathResource resource = new ClassPathResource("/upload/");
		String path = resource.getPath();

        File upFolder = new File(path);
        if (!upFolder.isDirectory()) {
            upFolder.mkdir();
        }
        
        Map<String, String> fmap = new HashMap<String, String>();

        // 대문이미지 - update
        MultipartFile ui = r_img;
        
        String oriname = ui.getOriginalFilename();
        sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
        
        fmap.put("rv_pk_num", String.valueOf(rv_pk_num));
        fmap.put("sysname", sysname);

        File fff = new File(path + sysname);
        ui.transferTo(fff);

        mainMapper.ReviewR_imgU(fmap);

        return sysname;
    }

    private AoptionDto aoptionGet(List<String> arrList) {
        AoptionDto apdto = new AoptionDto();
        
        for(int i = 0; i<arrList.size(); i++) {
           
           if(arrList.get(i).equals("a_waterpark")) {
                 apdto.setA_waterpark("1");
           } else if(arrList.get(i).equals("a_spa")){
                 apdto.setA_spa("1");
           } else if(arrList.get(i).equals("a_surfing")){
                 apdto.setA_surfing("1");
           } else if(arrList.get(i).equals("a_snowcooling")){
                 apdto.setA_snowcooling("1");
           } else if(arrList.get(i).equals("a_rafting")){
                 apdto.setA_rafting("1");
           } else if(arrList.get(i).equals("a_aquarium")){
                 apdto.setA_aquarium("1");
           } else if(arrList.get(i).equals("a_flowers")){
                 apdto.setA_flowers("1");
           } else if(arrList.get(i).equals("a_zoo")){
                 apdto.setA_zoo("1");
           } else if(arrList.get(i).equals("a_shooting")){
                 apdto.setA_shooting("1");
           } else if(arrList.get(i).equals("a_horse")){
                 apdto.setA_horse("1");
           } else if(arrList.get(i).equals("a_paragliding")){
                 apdto.setA_paragliding("1");
           } else if(arrList.get(i).equals("a_zipline")){
                 apdto.setA_zipline("1");
           } else if(arrList.get(i).equals("a_bike")){
                 apdto.setA_bike("1");
           } else if(arrList.get(i).equals("a_cart")){
                 apdto.setA_cart("1");
           } else if(arrList.get(i).equals("a_atv")){
                 apdto.setA_atv("1");
           } else if(arrList.get(i).equals("a_tourpass")){
                 apdto.setA_tourpass("1");
           } else if(arrList.get(i).equals("a_yort")){
                 apdto.setA_yort("1");
           } else if(arrList.get(i).equals("a_shipfishing")){
                 apdto.setA_shipfishing("1");
           } else if(arrList.get(i).equals("a_cablecar")){
                 apdto.setA_cablecar("1");
           } else if(arrList.get(i).equals("a_showpainting")){
                 apdto.setA_showpainting("1");
           } else if(arrList.get(i).equals("a_fishinglounge")){
                 apdto.setA_fishinglounge("1");
           } else if(arrList.get(i).equals("a_show")){
                 apdto.setA_show("1");
           } 
        }
        return apdto;
     }

    private LoptionDto loptionGet(List<String> arrList) {
        LoptionDto lpdto = new LoptionDto();
        
        for(int i = 0; i<arrList.size(); i++) {
           if(arrList.get(i).equals("l_cityview")) {
                 lpdto.setL_cityview("1");
           } else if(arrList.get(i).equals("l_seeview")) {
                 lpdto.setL_seeview("1");
           } else if(arrList.get(i).equals("l_skyview")) {
                 lpdto.setL_skyview("1");
           } else if(arrList.get(i).equals("l_outdoor")) {
                 lpdto.setL_outdoor("1");
           } else if(arrList.get(i).equals("l_petok")) {
                 lpdto.setL_petok("1");
           } else if(arrList.get(i).equals("l_bbq")) {
                 lpdto.setL_bbq("1");
           } else if(arrList.get(i).equals("l_cooking")) {
                 lpdto.setL_cooking("1");
           } else if(arrList.get(i).equals("l_pool")) {
                 lpdto.setL_pool("1");
           } else if(arrList.get(i).equals("l_wallpool")) {
                 lpdto.setL_wallpool("1");
           } else if(arrList.get(i).equals("l_spa")) {
                 lpdto.setL_spa("1");
           } else if(arrList.get(i).equals("l_garaoke")) {
                 lpdto.setL_garaoke("1");
           } 
        }
        return lpdto;
     }

    private ArrayList<String> loptionGet(LoptionDto ldto) {
        ArrayList<String> strarr = new ArrayList<String>();
  
        if (ldto.getL_cityview().equals("1")) {
           strarr.add("시티뷰");
        }
        if (ldto.getL_seeview().equals("1")) {
           strarr.add("바다뷰");
        }
        if (ldto.getL_skyview().equals("1")) {
           strarr.add("하늘뷰");
        }
        if (ldto.getL_outdoor().equals("1")) {
           strarr.add("야외테라스");
        }
        if (ldto.getL_petok().equals("1")) {
           strarr.add("애견동반");
        }
        if (ldto.getL_bbq().equals("1")) {
           strarr.add("바베큐");
        }
        if (ldto.getL_cooking().equals("1")) {
           strarr.add("취사");
        }
        if (ldto.getL_pool().equals("1")) {
           strarr.add("수영장");
        }
        if (ldto.getL_spa().equals("1")) {
           strarr.add("스파");
        }
        if (ldto.getL_wallpool().equals("1")) {
           strarr.add("월풀");
        }
        if (ldto.getL_garaoke().equals("1")) {
           strarr.add("노래방");
        }
  
        return strarr;
     }
  
     private ArrayList<String> aoptionGet(AoptionDto aodto) {
        ArrayList<String> strarr = new ArrayList<String>();
  
        if (aodto.getA_waterpark().equals("1")) {
           strarr.add("워터파크");
        }
        if (aodto.getA_spa().equals("1")) {
           strarr.add("스파/온천");
        }
        if (aodto.getA_surfing().equals("1")) {
           strarr.add("서핑/카약");
        }
        if (aodto.getA_snowcooling().equals("1")) {
           strarr.add("스노쿨링");
        }
        if (aodto.getA_rafting().equals("1")) {
           strarr.add("래프팅/보트");
        }
        if (aodto.getA_aquarium().equals("1")) {
           strarr.add("아쿠아리움");
        }
        if (aodto.getA_flowers().equals("1")) {
           strarr.add("수목원");
        }
        if (aodto.getA_zoo().equals("1")) {
           strarr.add("동물원");
        }
        if (aodto.getA_shooting().equals("1")) {
           strarr.add("사격");
        }
        if (aodto.getA_horse().equals("1")) {
           strarr.add("승마");
        }
        if (aodto.getA_paragliding().equals("1")) {
           strarr.add("패러글라이딩");
        }
        if (aodto.getA_zipline().equals("1")) {
           strarr.add("짚라인");
        }
        if (aodto.getA_bike().equals("1")) {
           strarr.add("레일바이크");
        }
        if (aodto.getA_cart().equals("1")) {
           strarr.add("카트");
        }
        if (aodto.getA_atv().equals("1")) {
           strarr.add("ATV");
        }
        if (aodto.getA_tourpass().equals("1")) {
           strarr.add("투어패스");
        }
        if (aodto.getA_yort().equals("1")) {
           strarr.add("유람선/요트");
        }
        if (aodto.getA_cablecar().equals("1")) {
           strarr.add("케이블카");
        }
        if (aodto.getA_shipfishing().equals("1")) {
           strarr.add("선상낚시");
        }
        if (aodto.getA_fishinglounge().equals("1")) {
           strarr.add("낚시터");
        }
        if (aodto.getA_showpainting().equals("1")) {
           strarr.add("전시");
        }
        if (aodto.getA_show().equals("1")) {
           strarr.add("공연");
        }
        return strarr;
    }

    
}
