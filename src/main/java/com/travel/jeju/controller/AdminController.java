package com.travel.jeju.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.jeju.dto.ListDto;
import com.travel.jeju.exception.AppException;
import com.travel.jeju.exception.ExceptionCode;
import com.travel.jeju.model.common.BaseModel;
import com.travel.jeju.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    
    @GetMapping("/company_list")
    public ResponseEntity<? extends BaseModel> Admin_Company_List(ListDto list) {

        Map<String, Object> result = adminService.selectCompanylist(list);

        BaseModel rs = new BaseModel(result);

        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("test")
    public ResponseEntity<? extends BaseModel> test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.warn("authentication => {}", authentication);

        log.warn("안뇽 +>");

        BaseModel rs = new BaseModel();

        return ResponseEntity.ok().body(rs);
    }
    
    
}
