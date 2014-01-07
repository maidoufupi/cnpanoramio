package com.cnpanoramio.webapp.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionTimeOutInterceptor implements HandlerInterceptor {
	private transient final Log log = LogFactory.getLog(getClass());
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("session time out Interceptor");
		if (request.getSession().getAttribute("user") == null) // 判断session里是否有用户信息
		{
			System.out.println("request no user");
			System.out.println(request.getHeader("x-requested-with"));
			System.out.println(request.getHeader("x-requested-with"));
			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase(
							"XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
			{
				response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
				return false;
			}

		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
