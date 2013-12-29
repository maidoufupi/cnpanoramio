package com.cnpanoramio.webapp.listener;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.UserSettingsManager;

public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {
    
	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserSettingsManager userSettingsService = null;
	
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event) {
		if (userSettingsService == null) {
			userSettingsService = WebApplicationContextUtils
					.getWebApplicationContext(
							httpSession.getServletContext()).getBean(
							UserSettingsManager.class);
		}
		AuthenticationSuccessEvent asEvent = (AuthenticationSuccessEvent) event;
        UserDetails userDetails = (UserDetails) asEvent.getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserSettings us = userSettingsService.getSettingsByUserName(username);
        httpSession.setAttribute("username", username);
        if(null != us) {
        	log.debug(username + ": " + us.getMapVendor());
        	httpSession.setAttribute("mapVendor", us.getMapVendor());
        }
    	
	}
	
	public UserSettingsManager getUserSettingsService() {
		return userSettingsService;
	}

	public void setUserSettingsService(UserSettingsManager userSettingsService) {
		this.userSettingsService = userSettingsService;
	}

}
