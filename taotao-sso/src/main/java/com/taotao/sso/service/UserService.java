package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	public TaotaoResult checkData(String content, Integer type) ;

	public TaotaoResult createUser(TbUser user);
	
	public TaotaoResult userLogin(HttpServletRequest request, HttpServletResponse response, String username, String password);

	public TaotaoResult getUserByToken(String token);
}
