package com.cnpanoramio.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.AvatarDao;
import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserResponse;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.UserUtil;

@Service("userSettingsService")
@Transactional
public class UserSettingsImpl implements UserSettingsManager {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserSettingsDao userSettingsDao;

	@Autowired
	private UserManager userManager;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private ViewsDao viewsDao;

	@Autowired
	private FavoriteDao favDao;

	@Autowired
	private UserDao userloginDao;

	@Autowired
	private AvatarDao avatarDao;

	@Autowired
	private FileService fileService;

	@Override
	public UserSettings save(UserSettings userSettings) {

		User user = getCurrentUser();
		UserSettings settings = userSettingsDao.get(user.getId());

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
		
		settings.setUser(user);

		userSettingsDao.save(settings);
		httpSession.setAttribute("mapVendor", settings.getMapVendor());

		return userSettings;
	}
	
	

	@Override
	public UserResponse.Settings getCurrentUserSettings() {

		UserSettings userSettings = null;

		User user = getCurrentUser();
		if (null != user) {
			userSettings = userSettingsDao.getByUserName(user.getUsername());
			return UserUtil.transformSettings(userSettings);
		}
		return null;
	}

	@Override
	public UserSettings getSettingsByUserName(String userName) {
		UserSettings userSettings = userSettingsDao.getByUserName(userName);
		return userSettings;
	}

	protected User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (null != auth) {
			Object principal = auth.getPrincipal();
			String username;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			return userManager.getUserByUsername(username);
		} else {
			return null;
		}

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

	@Override
	public UserOpenInfo getOpenInfo(Long id) {
		UserOpenInfo openInfo = new UserOpenInfo();
		UserSettings setting = userSettingsDao.get(id);
		openInfo.setId(setting.getId());

		// 用户头像
		if (null != setting.getAvatar()) {
			openInfo.setAvatar(setting.getAvatar().getId());
		} else {
			// 默认头像
			openInfo.setAvatar(0L);
		}

		// 用户名
		User user = userManager.get(id);
		openInfo.setName(user.getUsername());

		// 拥有照片数
		int count = photoDao.getPhotoCount(user);
		openInfo.setPhotoCount(count);

		// 总多少次被查看
		Long views = viewsDao.getUserPhotoViewCount(user);
		openInfo.setPhotoViews(views.intValue());

		// 多少张被收藏
		Long favs = favDao.getUserPhotoFavoriteCount(user);
		openInfo.setPhotoFavorites(favs.intValue());

		// 用户全部标签 tags
		List<String> tags = userSettingsDao.getUserTags(user);
		openInfo.setTags(tags);

		return openInfo;
	}

	@Override
	public User getUser(String nameOrEmail) {
		return (User) userloginDao.loadUserByUsername(nameOrEmail);
	}

	@Override
	public Avatar saveAvatar(User user, InputStream ins) {

		UserSettings settings = userSettingsDao.get(user.getId());
		Avatar avatar = new Avatar();
		avatar.setUserSettings(settings);
		avatar = avatarDao.save(avatar);

		settings.setAvatar(avatar);

		fileService.saveFile(FileService.TYPE_AVATAR, avatar.getId(),
				AVATAR_FILE_TYPE, ins);
		return avatar;
	}

	@Override
	public Avatar getAvatar(Long id) {
		return avatarDao.get(id);
	}

	@Override
	public Avatar getUserAvatar(Long userId) {
		return userSettingsDao.get(userId).getAvatar();
	}

	@Override
	public UserSettings create(User user) {
		UserSettings settings = null;
		try {
			settings = userSettingsDao.get(user.getId());
		}catch(ObjectRetrievalFailureException ex) {
			
		}
		
		if(null == settings) {
			settings = new UserSettings();
			settings.setUser(user);
			userSettingsDao.save(settings);
		}
		return settings;
	}

}
