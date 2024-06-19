package com.travel.jeju.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.travel.jeju.constant.FailType;
import com.travel.jeju.model.member.MemberModel;



public class CustomAuthenticationProvider implements AuthenticationProvider{

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsSecurityService userDetailsSecufityService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsSecufityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        //입력한 ID, Password 조회
        String username = token.getName();
        String password = (String)token.getCredentials();

        //UserDetailsService를 통해 DB에서 조회한 사용자
        MemberModel users = (MemberModel)userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new BadCredentialsException(FailType.PASS_NOT_MATCH.getValue());
        }

        return new UsernamePasswordAuthenticationToken(users, password, users.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    

}
