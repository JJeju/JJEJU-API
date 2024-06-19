package com.travel.jeju.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.constant.AuthConstants;
import com.travel.jeju.model.member.LoginRQ;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    // private LoggingQueryRepository loggingQueryRepository;

    // public CustomAuthenticationSuccessHandler (LoggingQueryRepository loggingQueryRepository){
    //     this.loggingQueryRepository = loggingQueryRepository;
    // }
    
    @Override
    
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        // TODO: 관리자 로직이 필요할 시 사용할 것
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Set<Authorities> authoritiesSet = authorities.stream()
        //     .map(authority -> {
        //         if (authority instanceof Authorities) {
        //             return (Authorities) authority;
        //         } else {
        //             return null;
        //         }
        //     })
        //     .filter(Objects::nonNull)
        //     .collect(Collectors.toSet());

        LoginRQ loginRQ = (LoginRQ)request.getSession().getAttribute(AuthConstants.LOGIN_INFO_SESSION.getValue());

        log.warn("Success Login");

        // Logging logging = new Logging();
        // logging.setType(LoggingType.LOGIN.getValue());
        // logging.setUsername(loginRQ.getUsername());
        // logging.setCredentials(loginRQ.getPassword());
        // logging.setMessage("Success Login");
        // logging.setIp(request.getRemoteAddr());

        // this.save(logging);

        
    }

    // @Transactional
    // private void save(Logging logging){
    //     loggingQueryRepository.insert(logging);
    // }

    /**
     * true일시 관리자, false 관리자 아님
     * @param authSet
     * @return
     */
    // private boolean adminCheck(Set<Authorities> authSet){

    //     int before = authSet.hashCode();
        
    //     authSet.removeIf(auth -> auth.getAuthority().equals(UserType.ADMIN.getValue()));

    //     int after = authSet.hashCode();

    //     return before != after;
    // }

}
