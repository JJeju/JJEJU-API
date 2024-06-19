package com.travel.jeju.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.travel.jeju.constant.UserType;
import com.travel.jeju.jwt.CustomAccessDeniedHandler;
import com.travel.jeju.jwt.CustomAuthenticationEntryPointHandler;
import com.travel.jeju.jwt.CustomAuthenticationFailureHandler;
import com.travel.jeju.jwt.CustomAuthenticationProvider;
import com.travel.jeju.jwt.CustomAuthenticationSuccessHandler;
import com.travel.jeju.jwt.JwtAuthenticationFilter;
import com.travel.jeju.jwt.JwtAuthorizationFilter;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.jwt.UserDetailsSecurityService;
import com.travel.jeju.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final UserDetailsSecurityService userDetailsSecufityService;

    private final MemberMapper memberMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Value("${url.base.file}")
    private String BASE_FLIE_URL;

    private final String[] PERMIT_ALL = {
        "/login",
        "/loginProc",

        "/member/insert",
        "/member/refresh_token",
        "/member/id_check",
        "/member/nick_check",
        "/member/send_sms",
        "/member/cert_sms",
        

        "/main/home",
        "/main/home/**",
        "/main/business_place",
        "/main/business_item",
        "/main/public_blog",

        "/mypage/blog/*",

        "/service-center/notice",
        "/service-center/notice/*",
        "/service-center/qa",
        "/service-center/qa/detail/*",

        // Common
        "/docs/index.html",
    };

    private final String[] COMPANY_AUTH = {
        "/company/**"
    };

    private final String[] ADMIN_AUTH = {
        "/admin/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(formLogin -> formLogin.disable());

        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests
                .requestMatchers(PERMIT_ALL).permitAll()
                .requestMatchers(COMPANY_AUTH).hasAnyAuthority(UserType.COMPANY.getDesc(),UserType.ADMIN.getDesc())
                .requestMatchers(ADMIN_AUTH).hasAnyAuthority(UserType.ADMIN.getDesc())
                .anyRequest().authenticated();
        });

        http.addFilter(jwtAuthenticationFilter());
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        

        http.exceptionHandling(exception -> {
            exception.accessDeniedHandler(this.customAccessDeniedHandler);
            exception.authenticationEntryPoint(this.customAuthenticationEntryPointHandler);
            
        });      

        return http.build();
    }

    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("x-timezone","Accept-Language","Accept","X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean(), jwtTokenProvider, memberMapper);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtTokenProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthentication(){
        return new CustomAuthenticationProvider(userDetailsSecufityService, bCryptPasswordEncoder());
    }
}
