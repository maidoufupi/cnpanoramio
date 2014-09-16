package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.json.UserResponse;
import com.cnpanoramio.json.UserSettings;
import com.cnpanoramio.json.UserSettingsResponse;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/settings")
public class SettingsRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager = null;

	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private UserSettingsService userSettingsService;
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public UserSettingsResponse getSettings(@PathVariable String userId) {
		UserSettingsResponse response = new UserSettingsResponse();
		
		// 检查操作的userId是否为登录的用户本人
		User user = isMy(Long.parseLong(userId), userManager);
		
		UserSettings settings = userSettingsService.getUserSettings(user);
		response.setSettings(settings);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/map", method = RequestMethod.POST)
	@ResponseBody
	public UserSettingsResponse changeMapVendor(@PathVariable String userId,
			@RequestBody final UserSettings settings) {
		UserSettingsResponse response = new UserSettingsResponse();
		
		log.debug("change MapVendor");
		log.debug(userId);
		log.debug(settings.getMapVendor());
		
		// 检查操作的userId是否为登录的用户本人
		User user = isMy(Long.parseLong(userId), userManager);
		
		userSettingsService.changeMapVendor(user, settings.getMapVendor());
		
		response.setStatus(UserSettingsResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/password", method = RequestMethod.POST)
	@ResponseBody
	public UserSettingsResponse changePassword(@PathVariable String userId,
			@RequestBody final UserSettingsResponse.Password password) {
		UserSettingsResponse response = new UserSettingsResponse();
		
		log.debug("change Password");
		log.debug(userId);
		log.debug(password.getCurrentPassword());
		
		// 检查操作的userId是否为登录的用户本人
		User user = isMy(Long.parseLong(userId), userManager);
		
		userSettingsService.changePassword(user, password);
		
		response.setStatus(UserSettingsResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/account", method = RequestMethod.POST)
	@ResponseBody
	public UserSettingsResponse changeAccount(@PathVariable String userId,
			@RequestBody final UserSettings settings) {
		UserSettingsResponse response = new UserSettingsResponse();
		
		log.debug("change account");
		log.debug(userId);
		log.debug(settings.getHomepageUrl());
		
		// 检查操作的userId是否为登录的用户本人
		User user = isMy(Long.parseLong(userId), userManager);
				
		userSettingsService.changeAccount(user, settings);
		
		response.setStatus(UserSettingsResponse.Status.OK.name());
		return response;
	}
	
	private User isMy(Long userId, UserManager userManager) {
		User me = UserUtil.getCurrentUser(userManager);
		if(!me.getId().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		return me;
	}
}
