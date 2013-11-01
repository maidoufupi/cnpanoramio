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

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class PhotoDaoTest {
	protected transient final Log log = LogFactory.getLog(getClass());

	Photo photo;
	User user;

	PhotoDao photoDao;
	UserDao userDao;

	public PhotoDao getPictureDao() {
		return photoDao;
	}

	@Autowired
	public void setPictureDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	@Before
	public void preMethodSetup() {
		
		user = new User();
		user.setFirstName("tw");
		user.setLastName("w");
		user.setUsername("any");
		user.setEmail("anypossible@foxmail.com");
		user.setPassword("123456");
		userDao.saveUser(user);
		
		photo = new Photo();
		photo.setFilePath("D:\\");
		photo.setFileType("jpg");
		photo.setName("myPicture");
		photo.setOwner(user);
		photoDao.save(photo);
		Assert.assertNotNull(photo.getId());
		log.info(photo.getId());
	}

	@After
	public void postMethodTearDown() {
		photoDao.remove(photo);
		userDao.remove(user);
	}

	@Test
	public void testPicturePersisted() {
		final List<Photo> photos = photoDao.getUserPhotos(user);
		Assert.assertEquals(1, photos.size());
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
		
}
