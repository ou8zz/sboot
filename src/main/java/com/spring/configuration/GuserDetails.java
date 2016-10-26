package com.spring.configuration;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spring.model.Guser;

/**
 * security 用户对象
 * @author OLE
 * @date 2016/10/26
 */
public class GuserDetails implements UserDetails {
	private static final long serialVersionUID = -5226602131176862620L;
	private Guser user;

	private Collection<GrantedAuthority> roles;
	
	public GuserDetails(Guser user, Collection<GrantedAuthority> roles) {
		this.user = user;
		this.roles = roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		if(user == null) {
			return null;
		}
		return user.getPwd();
	}

	@Override
	public String getUsername() {
		if(user == null) {
			return null;
		}
		return user.getEname();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public void setUser(Guser user) {
		this.user = user;
	}

	public Guser getGuser() {
		return user;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof GuserDetails) {
			if (this.user.getId().equals(((GuserDetails) object).user.getId()))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.user.getId().hashCode();
	}
}