package com.spring.controllers;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.services.AdminService;
import com.spring.services.GuserService;

/**
 * @description 后台用户管理
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/05/29
 * @version 1.0
 */
@Controller
@Scope(value="request")
public class IndexController {
//	private Log log = LogFactory.getLog(IndexController.class);
//	private RespsonData rd = new RespsonData("success");			// 通用返回JSON对象
	
	@Resource(name="guserService")
	private GuserService guserService;
	
	@Resource(name="adminService")
	private AdminService adminService;
	
	/**
	 * 空白首页
	 * @return
	 */
	@RequestMapping("/")
    public String home() {
        return "app/index";
    }
	
	/**
	 * 登录
	 */
	@RequestMapping(method=RequestMethod.GET, value="login")
	public String login() {
		return "login";
	}
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value={"/app/index", "/audit/index", "/admin/index"})
	public String rolePage(Model model, HttpServletRequest req) {
		return "app/index";
	}
}
