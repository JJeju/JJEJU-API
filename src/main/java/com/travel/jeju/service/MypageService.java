package com.travel.jeju.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.travel.jeju.constant.FileDescConstant;
import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.BlogDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.EventDto2;
import com.travel.jeju.dto.FavoritesDto;
import com.travel.jeju.dto.IamBasketDto;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.PastTripBlogDto;
import com.travel.jeju.dto.RevDto;
import com.travel.jeju.dto.ReviewDto;
import com.travel.jeju.dto.TravelPlanDto;
import com.travel.jeju.dto.TravelrouteDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.CompanyMapper;
import com.travel.jeju.mapper.FileMapper;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.mapper.MypageMapper;
import com.travel.jeju.mapper.TravelrouteMapper;
import com.travel.jeju.model.common.FileModel;
import com.travel.jeju.model.mypage.blog.create.CreateBlogRQ;
import com.travel.jeju.model.mypage.blog.select.MypageBlogDetailRS;
import com.travel.jeju.model.mypage.blog.select.MypageBlogParam;
import com.travel.jeju.model.mypage.blog.select.MypageBlogRQ;
import com.travel.jeju.model.mypage.blog.select.MypageBlogRS;
import com.travel.jeju.model.mypage.blog.update.MypageBlogAddPictureRQ;
import com.travel.jeju.model.mypage.blog.update.MypageBlogUpdateRQ;
import com.travel.jeju.model.mypage.deal.MypageDealParam;
import com.travel.jeju.model.mypage.deal.MypageDealRQ;
import com.travel.jeju.model.mypage.deal.MypageDealRS;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteParam;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteRS;
import com.travel.jeju.model.mypage.favorite.MypageFavoriteVO;
import com.travel.jeju.model.mypage.home.MypageHomeRS;
import com.travel.jeju.model.mypage.member.MypageMemberPasswordCheckRQ;
import com.travel.jeju.model.mypage.member.MypageMemberPasswordUpdateRQ;
import com.travel.jeju.model.mypage.member.MypageMemberUpdateRQ;
import com.travel.jeju.model.mypage.review.MypageReviewParam;
import com.travel.jeju.model.mypage.review.MypageReviewRS;
import com.travel.jeju.model.mypage.travel.MypageTravelDetailRS;
import com.travel.jeju.model.mypage.travel.MypageTravelParam;
import com.travel.jeju.model.mypage.travel.MypageTravelRS;
import com.travel.jeju.model.mypage.travel.MypageTravelUpdateComplateRQ;
import com.travel.jeju.model.travel.plan.DaysModel;
import com.travel.jeju.model.travel.plan.SelectPlanModel;
import com.travel.jeju.model.travel.plan.TravelPlanArraySortObject;
import com.travel.jeju.model.travel.plan.TravelPlanSelectRS;
import com.travel.jeju.util.FileUtil;
import com.travel.jeju.util.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final CompanyMapper companyMapper;

    private final TravelrouteMapper travelrouteMapper;

    private final MypageMapper mypageMapper;

    private final MemberMapper memberMapper;

    private final FileMapper fileMapper;

    private final FileUtil fileUtil;

    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder passwordEncoder;

    private final int listCount = 5;

    @Transactional
    public boolean blogCreate(CreateBlogRQ rq) {

        try {

            BlogDto param = new BlogDto();

            List<MultipartFile> files = rq.getFiles();

            if(files != null && !files.isEmpty()){
                Integer fileGroupNo = fileUtil.saveFile(rq.getFiles(), FileDescConstant.BLOG.getValue());

                param.setFile_group_no(fileGroupNo);
            }

            MultipartFile mainFile = rq.getMainFile();

            if(mainFile != null){

                Integer fileGroupNo = param.getFile_group_no();

                if(fileGroupNo != null){
                    fileUtil.updateFile(fileGroupNo, Arrays.asList(mainFile) , FileDescConstant.BLOG_MAIN.getValue());
                } else {
                    Integer groupFileNo = fileUtil.saveFile(Arrays.asList(mainFile), FileDescConstant.BLOG_MAIN.getValue());

                    param.setFile_group_no(groupFileNo);
                }
            }   

            String username = Principal.getUsername();

            param.setB_fk_tnum(rq.getTnum());
            param.setB_fk_id(username);
            param.setB_title(rq.getTitle());
            param.setB_cost(rq.getCost());
            param.setB_contents(rq.getContents());
            param.setB_create_dt(LocalDateTime.now());
            param.setB_public_check(rq.getPublicCheck());
            param.setB_star(rq.getStar());

            // TODO :: 후기블로그 추가해야함
            mypageMapper.createBlog(param);

        } catch (Exception e) {
            log.error("",e);
        }

        log.warn("dd");
        
        return true;
    }

    public boolean favoriteDelete(int fa_pk_num) {

        FavoritesDto favorite = mypageMapper.selectFavoriteById(fa_pk_num);

        if(favorite == null) return true;

        String fk_id = favorite.getFa_fk_id();
        String username = Principal.getUsername();

        if(!(fk_id.equals(username))) throw new AppException(ExceptionCode.NOT_PERMISSION);

        boolean result = true;

        try {
            mypageMapper.deleteFavoriteById(fa_pk_num);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }

        return result;
    }

    public MypageTravelRS travel(Boolean tr_tf) {

        String username = Principal.getUsername();

        MypageTravelParam param = new MypageTravelParam();
        param.setTr_tf(tr_tf);
        param.setUsername(username);

		List<TravelrouteDto> travel = mypageMapper.selectTravelByUsername(param);

        MypageTravelRS result = new MypageTravelRS();
        result.setTravel(travel);
		
        
        return result;
    }

    @Transactional
    public boolean travleUpdateComplate(MypageTravelUpdateComplateRQ rq) {

        TravelrouteDto travel = mypageMapper.selectTravelById(rq.getTr_pk_num());

        String fk_id = travel.getTr_fk_id();
        String username = Principal.getUsername();

        if(!(fk_id.equals(username))) throw new AppException(ExceptionCode.NOT_PERMISSION);

        boolean result = true;

        try {
            travel.setTr_tf(true);    
            
            mypageMapper.updateTravleComplate(travel);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }

        return result;
    }

    @Transactional
    public boolean travelDelete(int tr_pk_num) {

        TravelrouteDto travel = mypageMapper.selectTravelById(tr_pk_num);

        if(travel == null) return true;

        String fk_id = travel.getTr_fk_id();
        String username = Principal.getUsername();

        if(!(fk_id.equals(username))) throw new AppException(ExceptionCode.NOT_PERMISSION);
        
        boolean result = true;

        try {
            mypageMapper.deleteBasketByTnum(tr_pk_num);
            mypageMapper.deleteTravelById(tr_pk_num);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }
        
        return result;
    }

    public MypageTravelDetailRS travelDetail(int tr_pk_num) {
        TravelrouteDto travel = new TravelrouteDto();
        
        try {
            travel = mypageMapper.selectTravelById(tr_pk_num);
        } catch (Exception e) {
            log.error("",e);
        }
        
        List<BasketDto> bList = mypageMapper.selectBasketByTnum(tr_pk_num);
    
        List<EventDto2> eventList = new ArrayList<EventDto2>();
        EventDto2 eDto = new EventDto2();
        int price = 0;

        for(int i = 0; i<bList.size(); i++) {
            price += bList.get(i).getBk_price();
            eDto.setTitle(bList.get(i).getBk_goods());
            eDto.setStart(bList.get(i).getBk_in());
            eDto.setEnd(bList.get(i).getBk_out());
            eDto.setCnum(bList.get(i).getBk_fk_cnum());
            
            eventList.add(eDto);
        }

        MypageTravelDetailRS result = new MypageTravelDetailRS();
        result.setEvent(eventList);
        result.setPrice(price);
        result.setTravel(travel);
        
        return result;
    }

    @Transactional
    public boolean blogDelete(int b_pk_num) {

        boolean result = true;

        try {
            mypageMapper.deleteBimgByBnum(b_pk_num);
            mypageMapper.deleteBreplyByBnum(b_pk_num);
            mypageMapper.deleteBlogById(b_pk_num);
        } catch (Exception e) {
            log.error("",e);
            result = false;
        }

        return result;
    }
    
    @Transactional
    public boolean reviewDelete(Integer r_num) {

        ReviewDto dto = mypageMapper.selectReviewById(r_num);

        if(dto == null) return true;
        
        String username = Principal.getUsername();
        if(!(dto.getRv_fk_id().equals(username))) throw new AppException(ExceptionCode.NOT_PERMISSION);

        boolean result = mypageMapper.delreview(r_num);

        return result;
    }

    @Transactional
    public boolean memberPasswordUpdate(MypageMemberPasswordUpdateRQ rq) {

        String username = Principal.getUsername();
        MemberDto dto = memberMapper.memberSelect(username);

        boolean passCheck = passwordEncoder.matches(rq.getPassword(), dto.getM_pass());

        boolean result = true;

        if(passCheck){
            String newPassword = passwordEncoder.encode(rq.getNew_password());
            dto.setM_pass(newPassword);

            memberMapper.pwUpdate(dto);
        } else {
            result = false;
        }

        return result;
    }


    public boolean memberPasswordCheck(MypageMemberPasswordCheckRQ rq) {

        String username = Principal.getUsername();
        MemberDto dto = memberMapper.memberSelect(username);

        String userPassowrd = dto.getM_pass();
        String checkPassword = rq.getM_password();

        boolean result = passwordEncoder.matches(checkPassword, userPassowrd);

        return result;
    }

    @Transactional
    public boolean memberUpdate(MypageMemberUpdateRQ rq) {

        String username = Principal.getUsername();

        MemberDto dto = memberMapper.memberSelect(username);
        if(rq.getM_birth() != null && !(rq.getM_birth().equals(""))) dto.setM_birth(rq.getM_birth());
        if(rq.getM_email() != null && !(rq.getM_email().equals(""))) dto.setM_email(rq.getM_email());

        boolean result = true;

        try {
            memberMapper.memberUpdate(dto);
        } catch (Exception e) {
            result = false;

            throw new AppException(ExceptionCode.DB_ERROR);
        }

        return result;
    }

    @Transactional
    public boolean blogDeletePicture(int file_idx) {

        return fileMapper.deleteFile(file_idx);
    }

    @Transactional
    public boolean blogAddPicture(MypageBlogAddPictureRQ rq) {

        BlogDto blog = mypageMapper.selectMypageBlogById(rq.getB_pk_num());
        int file_group_no = blog.getFile_group_no();

        List<MultipartFile> file = Arrays.asList(rq.getB_img());

        boolean result = true;

        try {
            fileUtil.saveFile(file_group_no, file, FileDescConstant.BLOG.getValue());
        } catch (Exception e) {
            result = false;
        }
        

        return result;
    }

    @Transactional
    public boolean blogUpdate(MypageBlogUpdateRQ rq) {

        BlogDto blog = mypageMapper.selectMypageBlogById(rq.getB_pk_num());
        blog.setB_title(rq.getB_title());
        blog.setB_contents(rq.getB_contents());

        if(rq.getB_img() != null){

            Integer file_group_no = blog.getFile_group_no();
            List<MultipartFile> files = Arrays.asList(rq.getB_img());

            if(file_group_no != null){

                file_group_no = fileUtil.updateFile(file_group_no, files, FileDescConstant.BLOG_MAIN.getValue());
            } else {
                file_group_no = fileUtil.saveFile(files, FileDescConstant.BLOG_MAIN.getValue());
            }


            blog.setFile_group_no(file_group_no);
        }

        
        boolean updateCheck = mypageMapper.updateBlog(blog);

        boolean result = updateCheck;

        return result;
    }

    public MypageBlogDetailRS blogDetail(int bnum) {

        BlogDto blog = mypageMapper.selectMypageBlogById(bnum);
        
        if(blog == null) throw new AppException(ExceptionCode.DATA_NOT_FIND);

        Integer fileGroupNo = blog.getFile_group_no();

        MypageBlogDetailRS result = new MypageBlogDetailRS();

        if(fileGroupNo != null){
            List<FileModel> files = fileMapper.selectFileByFileGroupNo(blog.getFile_group_no());
    
            FileModel mainFile = files.stream().filter(value -> value.getDescription().contains(" Main")).findFirst().orElse(null);
            files = files.stream().filter(value -> !(value.getDescription().contains(" Main"))).toList();
            result.setFiles(files);
            result.setMainFile(mainFile);
        }

        Integer tnum = blog.getB_fk_tnum();

        TravelPlanSelectRS travelPlan = this.travelPlanSelect(tnum);
        
        result.setBlog(blog);
        result.setPlanList(travelPlan.getPlanList());
        result.setTravelroute(travelPlan.getTravelroute());
        
        return result;
    }

    public MypageBlogRS blog(MypageBlogRQ rq) {

        String username = Principal.getUsername();
        int pageNum = rq.getPageNum() == 0 ? 1 : rq.getPageNum();

        MypageBlogParam param = new MypageBlogParam();
        param.setUsername(username);
        param.setTitle(rq.getTitle());
        param.setPageNum((pageNum - 1) * listCount);
        param.setListCnt(this.listCount);

        List<BlogDto> blog = mypageMapper.selectMypageBlog(param);

        MypageBlogRS result = new MypageBlogRS();
        result.setBlog(blog);

        return result;
    }    

    public MypageReviewRS review(String category) {

        String username = Principal.getUsername();

        MypageReviewParam param = new MypageReviewParam();
        param.setCategory(category);
        param.setUsername(username);

        List<RevDto> review = mypageMapper.selectMypageReview(param);

        MypageReviewRS result = new MypageReviewRS();
        result.setReview(review);

        return result;
    }

    public MypageFavoriteRS favorite(String category) {

        String username = Principal.getUsername();

        MypageFavoriteParam param = new MypageFavoriteParam();
        param.setCategory(category);
        param.setUsername(username);

        List<MypageFavoriteVO> favorite = mypageMapper.selectFavorite(param);

        MypageFavoriteRS result = new MypageFavoriteRS();
        result.setFavorite(favorite);

        return result;
    }

    public MypageDealRS deal(MypageDealRQ rq) {

        int pageNum = rq.getPageNum() == 0 ? 1 : rq.getPageNum();
        String username = Principal.getUsername();

        MypageDealParam param = new MypageDealParam();
        param.setUsername(username);
        param.setCategory(rq.getCategory());
        param.setName(rq.getName());
        param.setPageNum((pageNum - 1) * listCount);
        param.setListCnt(this.listCount);

        List<IamBasketDto> deal = mypageMapper.selectDealList(param);

        MypageDealRS result = new MypageDealRS();
        result.setDeal(deal);

        return result;
    }

    public MypageHomeRS mypageHome() {

        String username = Principal.getUsername();

        List<IamBasketDto> payment = mypageMapper.selectMypagePayment(username);
        List<PastTripBlogDto> blog = mypageMapper.selectMypagePastTripBlog(username);

        MypageHomeRS result = new MypageHomeRS();
        result.setPayment(payment);
        result.setBlog(blog);

        return result;
    }
    

    public TravelPlanSelectRS travelPlanSelect(Integer tr_pk_num) {

        TravelrouteDto travel = travelrouteMapper.selectTravelById(tr_pk_num);
        String username = Principal.getUsername();

        if(travel == null) throw new AppException(ExceptionCode.DATA_NO);

        List<TravelPlanDto> travelPlanDtoList = travelrouteMapper.selectTravelPlanByPlanNum(tr_pk_num);

        final LocalDate tr_in = travel.getTr_in();
        final LocalDate tr_out = travel.getTr_out();
        
        DaysModel[] daysModels = this.createDaysArray(tr_in, tr_out);

        Map<Integer , CompanyDto> companyMap = new HashMap<>();

        for(TravelPlanDto dto : travelPlanDtoList){
            LocalDate planStartDate = dto.getTp_plan_start_date_time().toLocalDate();
            LocalDate planEndDate = dto.getTp_plan_end_date_time().toLocalDate();
            
            int mitnusStartDays = (int)ChronoUnit.DAYS.between(tr_in, planStartDate);
            int mitnusEndDays = (int)ChronoUnit.DAYS.between(tr_in, planEndDate);

            Integer cnum = dto.getTp_fk_company_num();

            CompanyDto companyNode = companyMap.get(cnum);

            if(companyNode == null){
                companyMap.put(cnum, companyMapper.selectCompanyById(cnum));
                companyNode = companyMap.get(cnum);
            }

            SelectPlanModel model = new SelectPlanModel();
            model.setTp_pk_num(dto.getTp_pk_num());
            model.setTp_fk_tnum(dto.getTp_fk_tnum());
            model.setTp_fk_company_info(companyNode);
            model.setTp_item_category(dto.getTp_item_category());
            model.setTp_plan_start_date_time(dto.getTp_plan_start_date_time());
            model.setTp_plan_end_date_time(dto.getTp_plan_end_date_time());
            model.setTp_rm(dto.getTp_rm());
            model.setCreate_dt(dto.getCreate_dt());
            model.setUpdate_dt(dto.getUpdate_dt());

            daysModels[mitnusStartDays].getDayPlanList().add(model);
            daysModels[mitnusEndDays].getDayPlanList().add(model);   
        }

        for(int i = 0; i < daysModels.length; i ++){
            List<SelectPlanModel> dayPlanList = daysModels[i].getDayPlanList();
            if(dayPlanList.size() == 1) continue;

            List<SelectPlanModel> newDayPlanList = new ArrayList<>();

            for(SelectPlanModel model : dayPlanList){
                if(!newDayPlanList.contains(model)){
                    newDayPlanList.add(model);
                }
            }

            newDayPlanList = this.sortingArrayByDate(newDayPlanList, tr_in.plusDays(i));

            daysModels[i].setDayPlanList(newDayPlanList);            
        }


        TravelPlanSelectRS result = new TravelPlanSelectRS();
        result.setPlanItemList(travelPlanDtoList);
        result.setPlanList(daysModels);
        result.setTravelroute(travel);

        return result;
    }

    private List<SelectPlanModel> sortingArrayByDate(List<SelectPlanModel> dayPlanList, LocalDate standardDate){
        
        List<TravelPlanArraySortObject> sortingList = new ArrayList<>();

        for(int i = 0 ; i < dayPlanList.size(); i ++){
            SelectPlanModel model = dayPlanList.get(i);

            TravelPlanArraySortObject node = new TravelPlanArraySortObject();
            node.setIndex(i);

            if(standardDate.isEqual(model.getTp_plan_start_date_time().toLocalDate())){
                node.setSortingDate(model.getTp_plan_start_date_time());
            } else {
                node.setSortingDate(model.getTp_plan_end_date_time());
            }

            sortingList.add(node);
        }

        Collections.sort(sortingList, Comparator.comparing(TravelPlanArraySortObject::getSortingDate));

        List<SelectPlanModel> newDayPlanList = new ArrayList<>();

        for(int i = 0; i < sortingList.size(); i++){

            newDayPlanList.add(dayPlanList.get(sortingList.get(i).getIndex()));
        }

        return newDayPlanList;
    }


    private DaysModel[] createDaysArray(LocalDate startDate, LocalDate endDate){
        
        int mitnusDays = (int)ChronoUnit.DAYS.between(startDate, endDate);

        DaysModel[] daysModels = new DaysModel[mitnusDays+1];

        for(int i = 0; i < daysModels.length; i ++){
            
            daysModels[i] = new DaysModel();
            daysModels[i].setDayPlanList(new ArrayList<>());
            daysModels[i].setDay(startDate);

            startDate = startDate.plusDays(1);
        }

        return daysModels;
    }
}
