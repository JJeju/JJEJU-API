package com.travel.jeju.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.travel.jeju.constant.AuthConstants;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.member.LoginRQ;
import com.travel.jeju.model.member.LoginRS;
import com.travel.jeju.model.member.TokenModel;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private MemberMapper memberMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, MemberMapper memberMapper){
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberMapper = memberMapper;
        setFilterProcessesUrl("/loginProc");
    }

    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRQ users = objectMapper.readValue(request.getInputStream(), LoginRQ.class);

            request.getSession().setAttribute(AuthConstants.LOGIN_INFO_SESSION.getValue(), users);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        TokenModel tokenModel = jwtTokenProvider.createToken(authResult);

        LoginRS result = new LoginRS();
        result.setToken(tokenModel);

        String jsonStr = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(result);

        response.setContentType("application/json");
        response.getWriter().write(jsonStr);

        MemberDto users = memberMapper.memberSelect(authResult.getName());

        users.setM_pass_fail_count(0);
        users.setM_refresh_token(tokenModel.getRefreshToken());

        memberMapper.loginSuccessUpdate(users);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    


}
