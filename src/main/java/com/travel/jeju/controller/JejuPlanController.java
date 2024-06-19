package com.travel.jeju.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.travel.jeju.dto.BreplyDto;
import com.travel.jeju.dto.JejuspotreviewDto;
import com.travel.jeju.dto.ListDto;
import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.service.JejuService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/jeju-plan")
public class JejuPlanController {

    private final JejuService jejuService;

    @GetMapping("/list")
    public ResponseEntity<? extends BaseModel> jejuplanList(@RequestBody ListDto list){

        Map<String, Object> result = jejuService.planList(list);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    @Deprecated
    @PostMapping(value = "/insert")
    public ResponseEntity<? extends BaseModel> jejuplanInsert (MultipartHttpServletRequest multi){

        boolean result = jejuService.jejuplanInsert(multi);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/jejuplan")
    public ResponseEntity<? extends BaseModel> jejuplan(@RequestParam int jnum){

        Map<String, Object> result = jejuService.jejuplanInfo(jnum);
        
        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/replyIns")
    public ResponseEntity<? extends BaseModel> replyInsert(BreplyDto reply){

        Map<String, List<BreplyDto>> result = jejuService.replyInsert(reply);

        BaseModel rs = new BaseModel(result);
                
        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/jejutourList")
    public ResponseEntity<? extends BaseModel> jejutourList(ListDto list){

        Map<String, Object> result = jejuService.getspotlist(list);

        BaseModel rs = new BaseModel(result);
                
        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/jejutour")
    public ResponseEntity<? extends BaseModel> jejutour (@RequestParam int jnum) {

        Map<String, Object> result = jejuService.spotinfo(jnum);

        BaseModel rs = new BaseModel(result);
                
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/reviewwrite")
    public ResponseEntity<? extends BaseModel> reviewwrite (MultipartHttpServletRequest multi){

        Map<String, String> rmap = new HashMap<String, String>();
		
		String res = null;
		
		try {
			jejuService.reviewInsert(multi);
			res = "ok";
		} catch (Exception e) {
			e.printStackTrace();
			res = "no";
		}

		rmap.put("res", res);

        BaseModel rs = new BaseModel(rmap);
                
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/redeletedelete")
    public ResponseEntity<? extends BaseModel> breDelete(String sv_pk_num) {

        String msg = jejuService.breDelete(sv_pk_num);

        Map<String, String> rmap = new HashMap<String, String>();
		rmap.put("res", msg);

        BaseModel rs = new BaseModel(rmap);
                
        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/replynumselect")
    public ResponseEntity<? extends BaseModel> replynumselect (String renum) {

        Map<String, JejuspotreviewDto> rmap = jejuService.replynumselect(renum);

        BaseModel rs = new BaseModel(rmap);
                
        return ResponseEntity.ok().body(rs);        
    }

    @PostMapping("/reviewupdate")
    public ResponseEntity<? extends BaseModel> reviewupdate (MultipartHttpServletRequest multi) {

        Map<String, String> rmap = new HashMap<String, String>();
		
		String res = null;
		
		try {
			jejuService.reviewUpdate(multi);
			res = "ok";
		} catch (Exception e) {
			e.printStackTrace();
			res = "no";
		}

        rmap.put("res", res);

        BaseModel rs = new BaseModel(rmap);
                
        return ResponseEntity.ok().body(rs);  
    }

    @GetMapping("jejuEvent_List")
    public ResponseEntity<? extends BaseModel> jejuEventList(ListDto list) {

        Map<String, Object> result = jejuService.eventList(list);
    
        BaseModel rs = new BaseModel(result);
                
        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/jejuEvent")
    public ResponseEntity<? extends BaseModel> jejuEventinfo(Integer e_pk_enum) {

        Map<String, Object> result = jejuService.eventInfoList(e_pk_enum);

        BaseModel rs = new BaseModel(result);
                
        return ResponseEntity.ok().body(rs);
    }


}
