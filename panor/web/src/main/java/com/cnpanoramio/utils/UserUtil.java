package com.cnpanoramio.utils;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserResponse;

public class UserUtil {

	public synchronized static User getCurrentUser(UserManager mgr) {
		Object obj = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String username;
		if (obj instanceof UserDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}

		return username != null ? mgr.getUserByUsername(username) : null;
	}
	
	public synchronized static UserResponse.Settings transformSettings(UserSettings userSettings) {
		UserResponse.Settings settings = new UserResponse.Settings();
		settings.setUserId(userSettings.getId());
		if(null != userSettings.getAvatar()) {
			settings.setUserAvatar(userSettings.getAvatar().getId());
		}else {
			settings.setUserAvatar(1L);
		}
		settings.setAlertComments(userSettings.getAlertComments());
		settings.setAlertGroupInvitations(userSettings.getAlertGroupInvitations());
		settings.setAlertPhotos(userSettings.getAlertPhotos());
		settings.setAllRightsReserved(userSettings.getAllRightsReserved());
		settings.setCommercialUse(userSettings.getCommercialUse());
		settings.setDescription(userSettings.getDescription());
		settings.setHomepageUrl(userSettings.getHomepageUrl());
		settings.setMapVendor(userSettings.getMapVendor());
		settings.setModify(userSettings.getModify());
		settings.setName(userSettings.getName());
		settings.setPrivateMessages(userSettings.getPrivateMessages());
		settings.setUrlName(userSettings.getUrlName());
		return settings;
	}
	
	public synchronized static UserSettings transformSettings(UserResponse.Settings userSettings) {
		UserSettings settings = new UserSettings();
		settings.setAlertComments(userSettings.getAlertComments());
		settings.setAlertGroupInvitations(userSettings.getAlertGroupInvitations());
		settings.setAlertPhotos(userSettings.getAlertPhotos());
		settings.setAllRightsReserved(userSettings.getAllRightsReserved());
		settings.setCommercialUse(userSettings.getCommercialUse());
		settings.setDescription(userSettings.getDescription());
		settings.setHomepageUrl(userSettings.getHomepageUrl());
		settings.setMapVendor(userSettings.getMapVendor());
		settings.setModify(userSettings.getModify());
		settings.setName(userSettings.getName());
		settings.setPrivateMessages(userSettings.getPrivateMessages());
		settings.setUrlName(userSettings.getUrlName());
		return settings;
	}
	
	public synchronized static UserOpenInfo getSimpleOpenInfo(UserSettings userSettings) {
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(userSettings.getId());
		openInfo.setName(userSettings.getName());
		if(null != userSettings.getAvatar()) {
			openInfo.setAvatar(userSettings.getAvatar().getId());
		}
		
		return openInfo;
	}
}
