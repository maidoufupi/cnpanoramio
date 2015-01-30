package com.cnpanoramio.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.weibo.Oauth;
import com.cnpanoramio.weibo.Users;
import com.cnpanoramio.weibo.http.AccessToken;
import com.cnpanoramio.weibo.model.User;
import com.cnpanoramio.weibo.model.WeiboException;

@Controller
public class WeiBoLandedController {
	private transient final Log log = LogFactory.getLog(getClass());
	@Autowired
	private UserSettingsManager userSettingsManager;

	@RequestMapping(value = "/weibo")
	public void listPage(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			Oauth oauth = new Oauth();
			String weibourl = oauth.authorize("code");
			try {
				response.sendRedirect(weibourl);
				// BareBonesBrowserLaunch.openURL();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/weiboToken", method = { RequestMethod.GET })
	public String weiBoTokenByCode(HttpServletRequest request,
			HttpServletResponse response, String code) {
		response.setContentType("text/html;charset=utf-8");
		try {
			Oauth oauth = new Oauth();
			AccessToken weibo = oauth.getAccessTokenByCode(code);
			Users um = new Users(weibo.getAccessToken());
			User weiBoUser = um.showUserById(weibo.getuid());
			request.getSession().setAttribute("weiBoUser", weiBoUser.getId());
			org.appfuse.model.User weiuser = userSettingsManager
					.createUserByWeiBo(weiBoUser);
			if (null != weiuser) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						weiuser.getUsername(), weiuser.getConfirmPassword(),
						weiuser.getAuthorities());
				auth.setDetails(weiuser);
				SecurityContextHolder.getContext().setAuthentication(auth);
				// Email为空时跳到注册页面
				if (weiuser.isCredentialsExpired() == true) {
					weiuser.setFirstName(weiBoUser.getName().toString());
					weiuser.setWebsite(weiBoUser.getProfileImageUrl()
							.toString());
					weiuser.setPhoneNumber("新浪微博");
					request.setAttribute("user", weiuser);
					return "TencenTregister/TencentEnroll2"; // 跳转注册用户页面
				}
			} else {
				log.error("创建用户失败"); // 登陆失败
				return "redirect:/#/signup";
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	@RequestMapping(value="/wei")
	public String weibo(){
		
		return "TencenTregister/TencentEnroll2"; // 跳转注册用户页面
	}
}
