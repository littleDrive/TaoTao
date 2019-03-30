package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {
		
		TaotaoResult result = null;
		//参数有效性校验
		if (StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "参数不能为空");
			
		} 
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
			
		}
		if (type != 1 && type != 2 && type != 3 ) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		//校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
		}
		
		try {
			result = userService.checkData(param, type);
		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result; 
		}
	}
	
	
	@RequestMapping("/register")
	@ResponseBody
	public TaotaoResult createUser(TbUser user) {
		
		try {
			TaotaoResult result = userService.createUser(user);
			return result;
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	
	//用户登录
		@RequestMapping(value="/login", method=RequestMethod.POST)
		@ResponseBody
		public TaotaoResult userLogin(HttpServletRequest request, HttpServletResponse response, String username, String password) {
			
			try {
				
				TaotaoResult result = userService.userLogin(request, response, username, password);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
			}
			
			
		}
	

		@RequestMapping("/token/{token}")
		@ResponseBody
		public Object getUserByToken(@PathVariable String token, String callback) {
			
			TaotaoResult result = null;
			try {
				result = userService.getUserByToken(token);
			} catch (Exception e) {
				e.printStackTrace();
				result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));			
			}
			
			//判断是否为jsonp调用
			if (StringUtils.isBlank(callback)) {
				return result;
			} else {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			
		}
		
		@RequestMapping("/showRegister")
		public String showRegister() {
			return "register";
		}
		
		
		@RequestMapping("/showLogin")
		public String showLogin(String redirect, Model model) {
			model.addAttribute("redirect", redirect);
			return "login";
		}
}
