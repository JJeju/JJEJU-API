package com.travel.jeju;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.jwt.UserDetailsSecurityService;
import com.travel.jeju.model.member.TokenModel;

import jakarta.servlet.ServletException;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

public abstract class BaseTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected UserDetailsSecurityService userDetailsSecurityService;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    public MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUpAll(RestDocumentationContextProvider restDocumentationContextProvider) throws ServletException{

        DelegatingFilterProxy delegateProxyFilter = new DelegatingFilterProxy();
        delegateProxyFilter.init(new MockFilterConfig(webApplicationContext.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));


        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext) // @Autowired로 빈주입 받은 context
            .addFilters(new CharacterEncodingFilter("UTF-8", true), delegateProxyFilter) // UTF-8 인코딩 필터
            .apply(
                MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider)
                .operationPreprocessors()
                .withRequestDefaults( // (1)
                    modifyUris().scheme("http").host("localhost").port(8080), prettyPrint() // URL 정보를 넣어주시면 됩니다.
                )
                .withResponseDefaults(
                    prettyPrint() // (2)
                )
            )
            .build();
    }

    protected TokenModel getAdminToken(){
        
        UserDetails user = userDetailsSecurityService.loadUserByUsername("lkd9125");

        TokenModel tokenModel = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(userToken);

        return tokenModel;
    }

    protected TokenModel getUserToken(){
        UserDetails user = userDetailsSecurityService.loadUserByUsername("영빡이");

        TokenModel tokenModel = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(userToken);

        return tokenModel;
    }

    protected TokenModel getUserToken(String test_id){
        UserDetails user = userDetailsSecurityService.loadUserByUsername(test_id);

        TokenModel tokenModel = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));

        return tokenModel;
    }

}
