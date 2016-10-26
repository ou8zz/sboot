package com.spring.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.model.Guser;

/**
 * security 登录用户校验
 * @author OLE
 * @date 2016/10/26
 */
public class UserDetailService implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String uname) {
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		Guser u = new Guser();
		u.setEname(uname);
		u.setPwd("c4ca4238a0b923820dcc509a6f75849b");
		try {
			GrantedAuthority granted = new SimpleGrantedAuthority("ROLE_USER");
			auths.add(granted);
		} catch (Exception e) {
		}
		// 获得用户的权限，设置权限
		GuserDetails gud = new GuserDetails(u, auths);
		return gud;
	}
}