package com.travel.jeju.model.member;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class MemberModel implements UserDetails{
	private String m_username;
	private String m_pass;
	private String m_name;
	private String m_gender;
	private String m_nickname;
	private String m_birth;
	private String m_phone;
	private String m_addr;
	private String m_email;
	private String m_category;
	private boolean m_enalbed;
	private boolean m_account_non_lock;
	private boolean m_account_non_expired;
	private int m_pass_fail_count;
	private String m_refresh_token;

	private List<GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.authorities;
	}

	@Override
	public String getPassword() {

		return this.m_pass;
	}

	@Override
	public String getUsername() {
		
		return this.m_username;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return this.m_account_non_expired;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return this.m_account_non_lock;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return this.m_enalbed;
	}

	
}
