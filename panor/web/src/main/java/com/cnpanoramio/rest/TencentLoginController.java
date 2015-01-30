package com.cnpanoramio.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.service.UserSettingsManager;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

@Controller
public class TencentLoginController {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserSettingsManager userSettingsManager;

	/**
	 * Tencent回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/tencentLogin", method = { RequestMethod.GET })
	public void tencentCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		try {
			response.sendRedirect(new Oauth().getAuthorizeURL(request));
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Othod2.0回调TencentAPI
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/tencentToken", method = { RequestMethod.GET })
	public String tencentToken(HttpServletRequest request,
			HttpServletResponse response, String code, String state)
			throws IOException {
		response.setContentType("text/html; charset=utf-8");
		try {
			AccessToken accessTokenObj = (new Oauth())
					.getAccessTokenByRequest(request);
			String accessToken = null, openID = null;
			long tokenExpireIn = 0L;

			if (accessTokenObj.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权， 统计下数据工作
				System.out.print("没有获取到响应参数");
			} else {
				accessToken = accessTokenObj.getAccessToken();
				tokenExpireIn = accessTokenObj.getExpireIn();
				request.getSession().setAttribute("photoAccessToken",
						accessToken);
				request.getSession().setAttribute("photoTokenExpirein",
						String.valueOf(tokenExpireIn));
				request.getSession(); // 利用获取到的accessToken 去获取当前用户的openID
				OpenID openIDObj = new OpenID(accessToken);
				openID = openIDObj.getUserOpenID();
				request.getSession().setAttribute("photoOpenId", openID);

				// 利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
				UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();

				// 0表示获取成功 1表示获取失败
				if (userInfoBean.getRet() == 0) {
					log.debug("用户基本信息" + userInfoBean.toString());
					User user = userSettingsManager.createUserByQQ(openID,
							userInfoBean);
					if (null != user) {
						UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
								user.getUsername(), user.getConfirmPassword(),
								user.getAuthorities());
						auth.setDetails(user);
						SecurityContextHolder.getContext().setAuthentication(
								auth);
						if (user.isCredentialsExpired() == true) {
							user.setFirstName(userInfoBean.getNickname()
									.toString());
							user.setWebsite(userInfoBean.getAvatar()
									.getAvatarURL100().toString());
							user.setPhoneNumber("腾讯QQ");
							request.setAttribute("user", user);
							return "TencenTregister/TencentEnroll2"; // 跳转注册用户页面
						}
					} else {
						log.error("创建用户失败"); // 登陆失败
						return "redirect:/#/signup";
					}
				} else {
					log.error("获取qq用户信息失败");
					userInfoBean.getMsg(); // 很抱歉，我们没能正确获取到您的信息
					return "redirect:/#/signup";
				}
			}
		} catch (QQConnectException e) {
			log.error("QQ链接失败");
		}
		return "redirect:/";
	}

	/**
	 * Ajax请求查询昵称存不存在： 1、存在提示此昵称已被被人使用 2、无则可以使用此呢称
	 * 
	 * @param request
	 * @param response
	 * @param nickname
	 * @return
	 */
	@RequestMapping(value = "/saveNickname", method = { RequestMethod.POST })
	@ResponseBody
	public int selNickname(HttpServletRequest request,
			HttpServletResponse response, String nickname) {
		log.debug("!!!!!!!!!!!!!"+request.getRequestURL());
		// JSONObject jsonObject = new JSONObject();
		// try{
		// jsonObject.put("nickname",
		// userSettingsManager.selNickname(nickname));
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		int ifNull = 0; // 判别昵称
		try {
			request.setCharacterEncoding("UTF-8");
			com.cnpanoramio.domain.UserSettings user = userSettingsManager
					.selNickname(nickname);
			if (user != null) {
				ifNull = 1;
				// name=user.getUser().getUsername();
				// URLDecoder.decode(name, "UTF-8");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ifNull;

	}

	/**
	 * 保存此注册用户
	 * 
	 * @param request
	 * @param response
	 * @param addUser
	 * @return
	 */
	@RequestMapping(value = "/addsaveuser", method = { RequestMethod.POST })
	public String addSaveUser(HttpServletRequest request,
			HttpServletResponse response, User addUser) {
		if (addUser.getPhoneNumber().equals("腾讯QQ")) {
			addUser.setWebsite((String) request.getSession().getAttribute(
					"photoOpenId"));
		} else if (addUser.getPhoneNumber().equals("新浪微博")) {
			addUser.setWebsite(request.getSession().getAttribute("weiBoUser")
					.toString());
		}
		addUser.setAccountExpired(false);
		userSettingsManager.addSaveUser(addUser);
		return "redirect:/"; // 返回首页
	}
}
