package com.travel.jeju.jwt;

import java.io.IOException;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // LogVO logVO = new LogVO();

        // try {
        //     logVO.setType("LOGOUT_SUCCESS");
		// 	logVO.setLoginId(authentication.getName());
        //     logVO.setIp(request.getRemoteAddr());
        // } catch (Exception e) {
        //     logVO.setType("LOGOUT_FAILURE");
		// 	logVO.setLoginId(authentication.getName());
        //     logVO.setIp(request.getRemoteAddr());
        // }

        // accessMapper.insertLog(logVO);

        log.warn("LOGOUT_SUCCESS");

        response.sendRedirect("/login");
    }
    
}
