package com.cnpanoramio.dao.hibernate;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.AvatarDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class UserSettingsDaoTest {
	protected transient final Log log = LogFactory.getLog(getClass());

	Photo photo;
	User user;
    
	@Autowired
	PhotoDao photoDao;
	UserDao userDao;
	
	@Autowired
	UserSettingsDao userSettingsDao;
	@Autowired
	AvatarDao avatarDao;

	UserSettings userSettings;

	@Before
	public void preMethodSetup() {
		
		user = new User();
		user.setFirstName("tw");
		user.setLastName("w");
		user.setUsername("anyp");
		user.setEmail("anypossible.w@foxmail.com");
		user.setPassword("123456");
		userDao.saveUser(user);
		
		userSettings = new UserSettings();
		userSettings.setCommercialUse(true);
		userSettings.setName("anypossiblew");
		userSettings.setUser(user);
	}

	@After
	public void postMethodTearDown() {
		userSettingsDao.remove(userSettings);
		userDao.remove(user);
	}

	@Test
	public void testUserSettingsPersisted() {
		
		userSettingsDao.save(userSettings);
		userSettings = userSettingsDao.getByUserName("any");
		log.info(userSettings.getName());
		Assert.assertFalse(userSettingsDao.getAll().size() == 0);
	}
	
	@Test
	public void testGetUserTags() {
		User user = userDao.get(3L);
		List<String> tags = userSettingsDao.getUserTags(user);
		
		log.info(tags.size());
		for(String tag : tags) {
			log.info(tag);
		}
	}
	
	@Test
	public void testAvatar() {
		userSettings = userSettingsDao.get(3L);
		Avatar avatar = new Avatar();
		avatar.setUserSettings(userSettings);
		avatar = avatarDao.save(avatar);
		userSettings.setAvatar(avatar);
		log.info(userSettings.getAvatar().getId());
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserSettingsDao getUserSettingsDao() {
		return userSettingsDao;
	}

	public void setUserSettingsDao(UserSettingsDao userSettingsDao) {
		this.userSettingsDao = userSettingsDao;
	}
	
	public PhotoDao getPictureDao() {
		return photoDao;
	}
	
	public void setPictureDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}
			
}
