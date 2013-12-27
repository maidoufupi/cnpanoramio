package com.cnpanoramio.service.impl;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;

@Service("userSettingsService")
@Transactional
public class UserSettingsImpl implements UserSettingsService, UserSettingsManager {

	private transient final Log log = LogFactory.getLog(UserSettingsService.class);
	
	@Autowired
	private UserSettingsDao userSettingsDao;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public Boolean save(UserSettings userSettings) {
		
		User user = getCurrentUser();
        
		userSettings.setUser(user);
		try {
			userSettingsDao.save(userSettings);
			httpSession.setAttribute("mapVendor", userSettings.getMapVendor());
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	@Override
	public UserSettings getCurrentUserSettings() {
		User user = getCurrentUser();
		UserSettings userSettings = userSettingsDao.getByUserName(user.getUsername());
		return userSettings;
	}
	
	@Override
	public UserSettings getSettingsByUserName(String userName) {
		UserSettings userSettings = userSettingsDao.getByUserName(userName);
		return userSettings;
	}
	
	protected User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		return userManager.getUserByUsername(username);
	}

	public UserSettingsDao getUserSettingsDao() {
		return userSettingsDao;
	}

	public void setUserSettingsDao(UserSettingsDao userSettingsDao) {
		this.userSettingsDao = userSettingsDao;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	

	
}
