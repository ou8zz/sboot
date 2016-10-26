package com.spring.services.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.spring.model.Guser;
import com.spring.services.UserService;

@Repository(value="userService")
@Scope(value="prototype")
public class UserServiceImpl implements UserService {
	
	@Override
	public Guser getUsers(Guser u) {
		return u;
	}
}
