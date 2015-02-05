package com.cnpanoramio.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.appfuse.Constants;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.appfuse.service.UserExistsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.AvatarDao;
import com.cnpanoramio.dao.CircleDao;
import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.RecycleDao;
import com.cnpanoramio.dao.TagDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.Circle;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.service.CircleManager;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;
import com.qq.connect.javabeans.qzone.UserInfoBean;

@Service
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
	
	@Autowired
	private TravelService travelService;
	
	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	private RecycleDao recycleDao;
	
	@Autowired
	private RoleManager roleManager;
	
	@Autowired
	private CircleDao circleDao;
	
	@Autowired
	private CircleManager circleManager;
	
	@Autowired
	private TagDao tagDao;
	
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDao userDao;
    @Autowired(required = false)
    private SaltSource saltSource;
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

	@Override
	public UserSettings save(UserSettings userSettings) {

		User user = UserUtil.getCurrentUser(userManager);
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
		settings.setAutoUpload(userSettings.isAutoUpload());
		
		settings.setUser(user);

		userSettingsDao.save(settings);
		httpSession.setAttribute("mapVendor", settings.getMapVendor());

		return userSettings;
	}

	@Override
	public UserSettings get(User user) {
		return userSettingsDao.get(user.getId());
	}

	@Override
	public UserSettings getSettingsByUserName(String userName) {
		UserSettings userSettings = userSettingsDao.getByUserName(userName);
		return userSettings;
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
	public UserOpenInfo getOpenInfo(User user) {
		User owner = UserUtil.getCurrentUser(userManager);
		return getOpenInfo(user, owner);
	}
	
	@Override
	public UserOpenInfo getOpenInfo(User user, User owner) {
		
		UserOpenInfo openInfo = new UserOpenInfo();
		
		UserSettings setting = userSettingsDao.get(user.getId());
		openInfo.setId(setting.getId());
		
		// 昵称
		openInfo.setName(setting.getName());
		
		// 描述
		openInfo.setDescription(setting.getDescription());
		
		// 用户头像
		if (null != setting.getAvatar()) {
			openInfo.setAvatar(setting.getAvatar().getId());
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
//		Long favs = favDao.getUserPhotoFavoriteCount(user);
//		openInfo.setPhotoFavorites(favs.intValue());

		// 用户全部标签 tags
		for(Tag tag : setting.getTags()) {
			openInfo.getTags().add(tag.getContent());
		}
		
		// 用户全部旅行
//		openInfo.setTravels(travelService.getTravels(user));
		
		// 登录用户是否follow此用户
		openInfo.setFollow(circleManager.isFollow(owner, user));

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
			// 默认用户名
			settings.setName(user.getUsername());
			userSettingsDao.save(settings);
		}
		return settings;
	}

	@Override
	public Set<String> getUserTags(User user) {
		UserSettings settings = userSettingsDao.get(user.getId());
		return convertTags(settings.getTags());
	}

	@Override
	public Tag createTag(User user, String tagContent) {
		UserSettings settings = userSettingsDao.get(user.getId());
		Tag tag = tagDao.getOrCreateUserTag(settings, tagContent);
		return tag;
	}

	@Override
	public Set<String> deleteTag(User user, String tag) {
		UserSettings settings = userSettingsDao.get(user.getId());
		Tag t = new Tag(tag);
		settings.getTags().remove(t);
		return convertTags(settings.getTags());
	}
	
	private Set<String> convertTags(Set<Tag> tags) {
		Set<String> list = new HashSet<String>();
		for(Tag t: tags) {
			list.add(t.getContent());
		}
		return list;
	}

	@Override
	public UserSettings get(Long id) {
		return userSettingsDao.get(id);
	}
		
	@Override
	public List<PhotoProperties> getPhotosForUserBounds(String id,
			int pageSize, int pageNo, Double swLat, Double swLng, Double neLat,
			Double neLng) {
		User user = userManager.getUser(id);
		
		List<Photo> photos = photoDao.getUserPhotosBounds(user, pageSize, pageNo, swLat, swLng, neLat, neLng);
		
		return PhotoUtil.transformPhotos(photos);
	}

	@Override
	public void following(User user, User following, boolean follow) {
		UserSettings userSetting = userSettingsDao.get(user.getId());
		UserSettings followingSetting = userSettingsDao.get(following.getId());
		Circle circle = null;
		if(userSetting.getCircles().isEmpty()) {
			circle = new Circle();
			circle.setName("好友");
			circle.setOwner(user);
			circle.setCreateDate(new Date());
			circle = circleDao.persist(circle);
		}else {
			circle = (Circle) userSetting.getCircles().toArray()[0];
		}

		if(follow) {
			circle.getUsers().add(following);
			followingSetting.getFollower().add(user);
		}else {
			circle.getUsers().remove(following);
			followingSetting.getFollower().remove(user);
		}
	}

	@Override
	public boolean isPasswordValid(User user, String password) {
		
		if (passwordEncoder != null) {
			String currentPassword = userDao.getUserPassword(user.getId());
			return passwordEncoder.isPasswordValid(currentPassword, password, saltSource.getSalt(user));
		} else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
            return false;
        }
	}

	@Override
	public void addStorageSpace(User user, Photo photo) {
		
		UserSettings userSetting = userSettingsDao.get(user.getId());
		Double space = 0D;
		if(null != userSetting.getStorageSpace()) {
			space = userSetting.getStorageSpace();
		}
		userSetting.setStorageSpace(space+photo.getFileSize());
		
	}

	@Override
	public void removeStorageSpace(User user, Photo photo) {
		UserSettings userSetting = userSettingsDao.get(user.getId());
		Double space = 0D;
		if(null != userSetting.getStorageSpace()) {
			space = userSetting.getStorageSpace();
		}
		userSetting.setStorageSpace(space-photo.getFileSize());
	}
	
	@Override
	public User createUserByQQ(String openID, UserInfoBean userInfoBean) {

		// UserSettings settings =
		// userSettingsDao.getByUserName(userInfoBean.getNickname());
		UserSettings settings = userSettingsDao.getByUserOpenId(openID);

		if (settings != null) {
			return userDao.get(settings.getId());
		} else {
			User user = new User();
			user.setUsername(userInfoBean.getNickname());
			user.setPassword("666666");
			// Set the default user role on this new user
			user.addRole(roleManager.getRole(Constants.USER_ROLE));
			user.setEnabled(true);
			user.setFirstName(userInfoBean.getNickname());
			user.setLastName(userInfoBean.getNickname());
			user.setEmail(openID);
			user.setCredentialsExpired(true);
			user.setWebsite(openID);

			// 用户默认详细设置
			UserSettings userSettings = null;
			try {
				user = userManager.saveUser(user);

				userSettings = create(user);

				userSettings.setName(userInfoBean.getNickname());
			} catch (AccessDeniedException ade) {
				// thrown by UserSecurityAdvice configured in aop:advisor
				// userManagerSecurity
				log.warn(ade.getMessage());
				return null;
			} catch (UserExistsException e) {
				// redisplay the unencrypted passwords
				// user.setPassword(user.getConfirmPassword());
				return null;
			}

			return user;
		}
	}

	@Override
	public UserSettings selNickname(String nickname) {
		return userSettingsDao.getByUserName(nickname);
	}

	@Override
	public void addSaveUser(User user) {
		userSettingsDao.addSaveUser(user);
		userSettingsDao.updateusersetting(user);
	}

	@Override
	public User createUserByWeiBo(com.cnpanoramio.weibo.model.User weiBoUser) {
		UserSettings settings = userSettingsDao.getByUserWeiBo(weiBoUser.getId());
		if (null != settings) {
			return userDao.get(settings.getId());
		} else {
			User user = new User();
			user.setUsername(weiBoUser.getName());
			user.setPassword("888888");
			user.addRole(roleManager.getRole(Constants.USER_ROLE));
			user.setEnabled(true);
			user.setFirstName(weiBoUser.getName());
			user.setLastName(weiBoUser.getId());
			user.setEmail(weiBoUser.getId());
			user.setCredentialsExpired(true);
			user.setWebsite(weiBoUser.getId());
			UserSettings userSettings = null;
			try {
				user = userManager.saveUser(user);
				userSettings = create(user);
				userSettings.setName(weiBoUser.getName());
			} catch (AccessDeniedException ade) {
				return null;
			} catch (UserExistsException e) {
				return null;
			}
			return user;
		}
	}
		
}
