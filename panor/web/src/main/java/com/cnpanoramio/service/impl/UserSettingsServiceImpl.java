package com.cnpanoramio.service.impl;

import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.ParameterException;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.AuthResponse.LoginRequest;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserSettings;
import com.cnpanoramio.json.UserSettingsResponse;
import com.cnpanoramio.service.CircleManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;

@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private UserManager userManager;

	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private CircleManager circleManager;

	@Autowired
	private PhotoDao photoDao;
	
	@Autowired
	private ViewsDao viewsDao;

	@Override
	public void save(UserSettings settings) {
		userSettingsManager.save(transformSettings(settings));
	}

	@Override
	public UserOpenInfo getOpenInfo(User user, User me) {

		UserOpenInfo openInfo = new UserOpenInfo();

		com.cnpanoramio.domain.UserSettings userSettings = userSettingsManager.get(user);
		
		openInfo.setId(userSettings.getId());

		// 昵称
		openInfo.setName(userSettings.getName());

		// 描述
		openInfo.setDescription(userSettings.getDescription());

		// 用户头像
		if (null != userSettings.getAvatar()) {
			openInfo.setAvatar(userSettings.getAvatar().getId());
		} else {
			// 默认头像
			openInfo.setAvatar(0L);
		}

		// 系统用户名
		user = userManager.get(user.getId());
		openInfo.setUsername(user.getUsername());

		// 拥有照片数
		Long count = photoDao.getPhotoCount(user);
		openInfo.setPhotoCount(count);

		// 总多少次被查看
		Long views = viewsDao.getUserPhotoViewCount(user);
		openInfo.setPhotoViews(views.intValue());

		// 多少张被收藏
		// Long favs = favDao.getUserPhotoFavoriteCount(user);
		// openInfo.setPhotoFavorites(favs.intValue());

		// 用户全部标签 tags
		for (Tag tag : userSettings.getTags()) {
			openInfo.getTags().add(tag.getContent());
		}

		if(null != me) {
			// 登录用户是否follow此用户
			openInfo.setFollow(circleManager.isFollow(me, user));
		}		

		return openInfo;
	}

	@Override
	public UserSettings getUserSettings(User user) {

		com.cnpanoramio.domain.UserSettings userSettings = null;

		if (null != user) {
			userSettings = userSettingsManager.get(user);
			return transformSettings(userSettings);
		}
		return null;
	}

	@Override
	public void changePassword(User user, UserSettingsResponse.Password password) {

		if (!userSettingsManager.isPasswordValid(user,
				password.getCurrentPassword())) {
			throw new ParameterException("currentPassword");
		}
		user = userManager.get(user.getId());
		user.setPassword(password.getPassword());
		try {
			userManager.saveUser(user);
		} catch (UserExistsException e) {
			throw new ParameterException("password");
		}
	}

	@Override
	public UserSettings changeMapVendor(User user, MapVendor mapVendor) {
		com.cnpanoramio.domain.UserSettings settings = userSettingsManager
				.get(user);
		settings.setMapVendor(mapVendor);

		httpSession.setAttribute("mapVendor", settings.getMapVendor());
		
		return transformSettings(settings);
	}

	@Override
	public UserSettings changeAccount(User user, UserSettings settings) {
		com.cnpanoramio.domain.UserSettings userSettings = userSettingsManager
				.get(user);
		userSettings.setName(settings.getName());
		userSettings.setHomepageUrl(settings.getHomepageUrl());
		userSettings.setDescription(settings.getDescription());

		return transformSettings(userSettings);
	}
	
	@Override
	public boolean loginCheck(LoginRequest loginRequest) {
		User user = userSettingsManager.getUser(loginRequest.getUsername());
		if (!userSettingsManager.isPasswordValid(user,
				loginRequest.getPassword())) {
			throw new ParameterException("password");
		}
		return true;
	}

	public UserSettings transformSettings(
			com.cnpanoramio.domain.UserSettings userSettings) {
		UserSettings settings = new UserSettings();
		settings.setUserId(userSettings.getId());
		if (null != userSettings.getAvatar()) {
			settings.setUserAvatar(userSettings.getAvatar().getId());
		} else {
			settings.setUserAvatar(1L);
		}
		settings.setAlertComments(userSettings.getAlertComments());
		settings.setAlertGroupInvitations(userSettings
				.getAlertGroupInvitations());
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
		settings.setStorageSpace(userSettings.getStorageSpace());
		settings.setAutoUpload(userSettings.isAutoUpload());
		return settings;
	}

	public com.cnpanoramio.domain.UserSettings transformSettings(
			UserSettings userSettings) {
		com.cnpanoramio.domain.UserSettings settings = new com.cnpanoramio.domain.UserSettings();
		settings.setAlertComments(userSettings.getAlertComments());
		settings.setAlertGroupInvitations(userSettings
				.getAlertGroupInvitations());
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
		settings.setAutoUpload(userSettings.isAutoUpload());
		return settings;
	}

	@Override
	public UserSettings changeUpload(User user, UserSettings userSettings) {
		com.cnpanoramio.domain.UserSettings settings = userSettingsManager
				.get(user);
		
		settings.setAutoUpload(userSettings.isAutoUpload());

		return transformSettings(settings);
	}

	

}
