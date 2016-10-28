package com.spring.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.model.Guser;
import com.utils.RegexUtil;

/**
 * security 登录用户校验
 * @author OLE
 * @date 2016/10/26
 */
public class UserDetailService implements UserDetailsService {
	private static final Log log = LogFactory.getLog(UserDetailService.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public UserDetails loadUserByUsername(String uname) {
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		Guser u = new Guser();
		u.setEname(uname);
		try {
			u = sqlSession.selectOne("guser.getGusers", u);
			if(u ==null){
				log.error("user \"" + uname + "\" not exist, please check again!");
			} else {
				u = sqlSession.selectOne("guser.getGuserDetil", new Guser(u.getId()));
				if(RegexUtil.notEmpty(u.getRole())) {
					GrantedAuthority granted = new SimpleGrantedAuthority("ROLE_"+u.getRole());
					auths.add(granted);
				}
				if(RegexUtil.notEmpty(u.getPosition())) {
					GrantedAuthority granted = new SimpleGrantedAuthority("ROLE_"+u.getPosition());
					auths.add(granted);
				}
			}
		} catch (Exception e) {
			log.error("get user role is faild", e);
		}
		// 获得用户的权限，设置权限
		GuserDetails gud = new GuserDetails(u, auths);
		return gud;
	}
}