package com.travel.jeju.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.travel.jeju.constant.FailType;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.member.MemberModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsSecurityService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {

        MemberDto usersWrap = memberMapper.memberSelect(username);

        if(usersWrap == null){
            throw new UsernameNotFoundException(FailType.USER_NOT_FOUNT.getValue());
        } 

        List<String> userAuth =  memberMapper.memberAuthoritiesSelect(username);

        List<GrantedAuthority> authorities = userAuth.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        MemberModel users = new MemberModel();
        users.setM_username(username);
        users.setM_pass(usersWrap.getM_pass());
        users.setM_name(usersWrap.getM_name());
        users.setM_gender(usersWrap.getM_gender());
        users.setM_nickname(usersWrap.getM_nickname());
        users.setM_birth(usersWrap.getM_birth());
        users.setM_phone(usersWrap.getM_phone());
        users.setM_addr(usersWrap.getM_addr());
        users.setM_email(usersWrap.getM_email());
        users.setM_category(usersWrap.getM_category());
        users.setM_enalbed(usersWrap.isM_enabled());
        users.setM_account_non_lock(usersWrap.isM_account_non_lock());
        users.setM_account_non_expired(usersWrap.isM_account_non_expired());
        users.setM_pass_fail_count(usersWrap.getM_pass_fail_count());
        users.setM_refresh_token(usersWrap.getM_refresh_token());

        users.setAuthorities(authorities);

        

        if(!users.isEnabled()){
            throw new BadCredentialsException(FailType.ACOUNT_DISABLE.getValue());
        }
       
        if(!users.isAccountNonExpired()){
            throw new BadCredentialsException(FailType.ACOUNT_EXPRIED.getValue());
        }

        if(!users.isAccountNonLocked()){
            throw new BadCredentialsException(FailType.ACOUNT_LOCK.getValue());
        }


        return users;
    }

}
