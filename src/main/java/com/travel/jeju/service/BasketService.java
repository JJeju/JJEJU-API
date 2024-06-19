package com.travel.jeju.service;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.dto.AactivityDto;
import com.travel.jeju.dto.BasketDto;
import com.travel.jeju.dto.CompanyDto;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.RoomDto;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.BasketMapper;
import com.travel.jeju.mapper.CompanyMapper;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.basket.InsertBasketRQ;
import com.travel.jeju.model.basket.InsertBasketReserveRQ;
import com.travel.jeju.util.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketMapper basketMapper;

    private final CompanyMapper companyMapper;

    private final MemberMapper memberMapper;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Map<String, Object> basketDelete(String trnum, String bk_pk_num) {

        log.info("MainService - lBasketDel()");
        Map<String, Object> result = new HashMap<String, Object>();

        List<BasketDto> lList = new ArrayList<BasketDto>();
        List<BasketDto> aList = new ArrayList<BasketDto>();
        List<BasketDto> fList = new ArrayList<BasketDto>();
        
        try {
           basketMapper.deleteBasketById(bk_pk_num);
           lList = basketMapper.selectLodCompany(trnum);
           aList = basketMapper.selectActCompany(trnum);
           fList = basketMapper.selectFoodCompany(trnum);

        } catch (Exception e) {
           log.error("",e);
        }
        
        result.put("lList", lList);
        result.put("aList", aList);
        result.put("fList", fList);
        
        return result;
    }

    // TODO :: 예약건 없는 일정 [레저, 식당] , 여행건 불러와서 여행 일정 validation 해야함
    @Transactional
    public boolean basketInsert(InsertBasketRQ rq) {
        log.info("MainService - getAplan()");
        Map<String, Object> omap = new HashMap<String, Object>();

        BasketDto bdto = new BasketDto();
        bdto.setBk_fk_cnum(rq.getBk_fk_cnum());
        bdto.setBk_fk_num(rq.getBk_fk_num());
        bdto.setBk_fk_tnum(rq.getBk_fk_tnum());
        bdto.setBk_goods(rq.getBk_goods());
        bdto.setBk_people(rq.getBk_people());

        CompanyDto cdto = companyMapper.selectCompanyById(rq.getBk_fk_cnum());

        if(cdto.getC_category().equals("레저")) {
            AactivityDto atdto = companyMapper.selectAactivityById(String.valueOf(bdto.getBk_fk_num()));

            if(atdto.getAt_rtch().equals("1")) {
                omap.put("at_rtch", atdto.getAt_rtch());
            }

            bdto.setBk_price(atdto.getAt_price() * bdto.getBk_people());
        } else {

            RoomDto rdto = companyMapper.selectRoomById(bdto.getBk_fk_num());

            bdto.setBk_price(rdto.getR_price() * bdto.getBk_people());
        }
        
        LocalDateTime date = rq.getDate();

        if(date!= null) {
            bdto.setBk_in(date);
            bdto.setBk_out(date);
        }

        String token = jwtTokenProvider.getHeaderToken();
        String username = jwtTokenProvider.getSubject(token);

        bdto.setBk_fk_id(username);

        AactivityDto atdto = companyMapper.selectAactivityById(String.valueOf(bdto.getBk_fk_num()));
        log.info("종류 => {}", cdto.getC_category());

        omap.put("at_rtch", atdto.getAt_rtch());
        omap.put("cate", cdto.getC_category());
        omap.put("basdto", bdto);

        boolean result = true;

        try {
            basketMapper.insertBasket(omap);
        } catch (Exception e) {
            log.error("",e);
            result = false;
        }

        return result;
    }

    @Transactional
    public boolean basketReserveInsert(InsertBasketReserveRQ rq) {
        Map<String, Object> omap = new HashMap<String, Object>();

        String username = Principal.getUsername();

        MemberDto mdto = memberMapper.memberSelect(username);
        CompanyDto cdto = companyMapper.selectCompanyById(rq.getBk_fk_cnum());

        BasketDto basdto = new BasketDto();
        basdto.setBk_people(rq.getBk_people());
        basdto.setBk_in(rq.getBk_in());
        basdto.setBk_out(rq.getBk_out());
        basdto.setBk_number(rq.getBk_number());
        basdto.setBk_name(rq.getBk_name());
        basdto.setBk_fk_num(rq.getBk_fk_num());
        basdto.setBk_carch(rq.getBk_carch());

        if(cdto.getC_category().equals("레저")) {
            AactivityDto atdto = companyMapper.selectAactivityById(String.valueOf(basdto.getBk_fk_num()));

            if(atdto.getAt_rtch().equals("1")) {
                omap.put("at_rtch", atdto.getAt_rtch());
            }

            basdto.setBk_price(atdto.getAt_price() * basdto.getBk_people());
        } else {

            RoomDto rdto = companyMapper.selectRoomById(basdto.getBk_fk_num());

            basdto.setBk_price(rdto.getR_price() * basdto.getBk_people());
        }

        switch (basdto.getBk_carch()) {
            case "도보" :
                basdto.setBk_carch("0");
                break;
            case "차량" :
                basdto.setBk_carch("1");
                break;
        }

        basdto.setBk_fk_id(mdto.getM_username());
        basdto.setBk_fk_cnum(rq.getBk_fk_cnum());
        basdto.setBk_goods(rq.getBk_goods());
        basdto.setBk_fk_tnum(rq.getBk_fk_tnum()); // 여행번호

        log.info("상품 이름 => {}", basdto.getBk_goods());

        omap.put("basdto", basdto);
        omap.put("cate", cdto.getC_category());

        String cate = cdto.getC_category();

        switch(cate) {
            case "숙박" : 
                cate = "'숙박'";

                try {
                    cate = URLEncoder.encode("'숙박'", "UTF-8");
                } catch (Exception e) {
                    log.error("",e);
                }

                break;
            case "레저":
                cate = "'레저'";

                try {
                    cate = URLEncoder.encode("'레저'", "UTF-8");
                } catch (Exception e) {
                    log.error("",e);
                }

                break;
            case "식당":
                cate = "'식당'";

                try {
                    cate = URLEncoder.encode("'식당'", "UTF-8");
                } catch (Exception e) {
                    log.error("",e);
                }

                break;
        }

        boolean result = true;

        try {
            basketMapper.insertBasket(omap);
        } catch (Exception e) {
            log.error("",e);

            result = false;
        }

        return result;
    }

}
