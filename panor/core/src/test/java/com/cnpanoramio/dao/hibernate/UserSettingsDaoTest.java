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
import com.cnpanoramio.domain.Recycle;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class UserSettingsDaoTest {
	protected transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	PhotoDao photoDao;
	
	@Autowired
	UserSettingsDao userSettingsDao;
	@Autowired
	UserDao userDao;
	@Autowired
	AvatarDao avatarDao;

	private UserSettings userSettings;
	private User user;
	private Photo photo;

	@Before
	public void preMethodSetup() {
		userSettings = userSettingsDao.get(1L);
		user = userDao.get(1L);
//		user = new User();
//		user.setFirstName("tw");
//		user.setLastName("w");
//		user.setUsername("anyp");
//		user.setEmail("anypossible.w@foxmail.com");
//		user.setPassword("123456");
//		userDao.saveUser(user);
		
//		userSettings = new UserSettings();
//		userSettings.setCommercialUse(true);
//		userSettings.setName("anypossiblew");
//		userSettings.setUser(user);
	}

	@After
	public void postMethodTearDown() {
//		userSettingsDao.remove(userSettings);
//		userDao.remove(user);
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
		User user = userDao.get(1L);
		List<String> tags = userSettingsDao.getUserTags(user);
		
		log.info(tags.size());
		for(String tag : tags) {
			log.info(tag);
		}
	}
	
	@Test
	public void testAvatar() {
		userSettings = userSettingsDao.get(1L);
		Avatar avatar = new Avatar();
		avatar.setUserSettings(userSettings);
		avatar = avatarDao.save(avatar);
		userSettings.setAvatar(avatar);
		log.info(userSettings.getAvatar().getId());
	}
	
	@Test
	public void testCreateTag() {
		
		userSettingsDao.createTag(userSettings, "四川");
		
		userSettings.getTags().add(new Tag("上海"));
		userSettings.getTags().add(new Tag("上海"));
		userSettings = userSettingsDao.save(userSettings);
		for(Tag tag: userSettings.getTags()) {
			log.info(tag);
		}
		UserSettings userSettings2 = userSettingsDao.get(2L);
		userSettings2.getTags().add(new Tag("上海"));
		userSettings2 = userSettingsDao.save(userSettings2);
		for(Tag tag: userSettings2.getTags()) {
			log.info(tag);
		}
		
	}
	
	@Test
	public void testGetOrCreateUserTag() {
		
		Tag tag = userSettingsDao.getOrCreateUserTag(userSettings, "上海");
		log.info(tag);
	}
	
	@Test
	public void testDeleteTag() {
		userSettingsDao.createTag(userSettings, "四川");
		userSettingsDao.createTag(userSettings, "上海");
		userSettings.getTags().remove(new Tag("四川"));
		for(Tag tag: userSettings.getTags()) {
			log.info(tag);
		}
	}
	
	@Test
	public void testEmptyRecycle() {
		Recycle recycle = new Recycle();
		UserSettings setting = userSettingsDao.get(1L);
		setting.getRecycle().add(recycle);
		Assert.assertEquals(1, setting.getRecycle().size());
		setting.getRecycle().clear();
		Assert.assertEquals(0, setting.getRecycle().size());
	}
	
	@Test
	public void testSearch() {
		List<User> users = userDao.search("user");
		Assert.assertEquals(1, users.size());
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
