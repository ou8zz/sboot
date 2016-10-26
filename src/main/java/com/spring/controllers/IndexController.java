package com.spring.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.Guser;
import com.spring.services.UserService;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.GET, value={"/login"})
	public String login() {
		return "login";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/index")
	@ResponseBody
	public Object index(Model model) {
		Guser u = new Guser();
		u.setCname("中国");
		u.setEname("china");
		u.setId(1);
		u.setBirthday(new Date());
		Guser su = userService.getUsers(u);
		return su;
	}
}
