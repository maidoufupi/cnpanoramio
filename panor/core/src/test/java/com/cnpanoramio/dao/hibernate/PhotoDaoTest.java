package com.cnpanoramio.dao.hibernate;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Favorite;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Tag;

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
	
	private Long userId;

	public PhotoDao getPictureDao() {
		return photoDao;
	}

	@Autowired
	public void setPictureDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	@Before
	public void preMethodSetup() {
		userId = 1L;
	}

	@After
	public void postMethodTearDown() {
		
	}

	@Test
	public void testPicturePersisted() {
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
		final List<Photo> photos = photoDao.getUserPhotos(user);
		Assert.assertEquals(1, photos.size());
		
		photoDao.remove(photo);
		userDao.remove(user);
	}
	
	@Test
	public void testPhotoDetials() {
		photo = new Photo();
		photo.setFilePath("D:\\");
		photo.setFileType("jpg");
		photo.setName("myPicture");
//		photo.setOwner(user);
		PhotoDetails details = new PhotoDetails();
		details.setBrightnessValue(new Double(123));
		details.setPhoto(photo);
		photo.setDetails(details);
		photoDao.save(photo);
		photo = photoDao.get(photo.getId());
		Assert.assertNotNull(photo.getDetails());
	}
	
	@Test
	public void testGetPhotoCount() {
		User user = userDao.get(1L);
		int count = photoDao.getPhotoCount(user);
		log.info(count);
	}
	
	@Test
	public void testGetUserPhotos() {
		User user = userDao.get(userId);
		List<Photo> photos = photoDao.getUserPhotos(user, 10, 1);
		for(Photo photo : photos) {
			log.info("All : " + photo.getId());
		}
		photos = photoDao.getUserPhotos(user, 10, 1);
		for(Photo photo : photos) {
			log.info("Page : " + photo.getId());
		}
		Assert.assertEquals(10, photos.size());
	}

	@Test
	public void testAddPhotoTags() {
		photo = new Photo();
		photo.addTag(new Tag("This is a tag"));
		photo.addTag(new Tag("tagA"));
		photo.addTag(new Tag("tagA"));
		photo = photoDao.save(photo);
		Assert.assertTrue(null != photo.getId());
		Assert.assertTrue(0 != photo.getId());
		Assert.assertTrue(photo.getTags().size() == 2);
		photo.getTags().clear();
		photo.addTag(new Tag("tagB"));
		photo = photoDao.save(photo);
		Assert.assertTrue(null != photo.getId());
		Assert.assertTrue(0 != photo.getId());
		log.info(photo.getTags());
		Assert.assertEquals(1, photo.getTags().size());
		photo = new Photo();
//		photo.addTag(new Tag("This is a tag"));
		photo.addTag(new Tag("tagA"));
		photo = photoDao.save(photo);
		Assert.assertTrue(null != photo.getId());
		Assert.assertTrue(0 != photo.getId());
		log.info(photo.getTags());
		Assert.assertEquals(1, photo.getTags().size());
	}
	
	@Test
	public void testAddFavorite() {
		
		Photo photo = photoDao.get(1L);
//		photo.addFavorite(new Favorite(1L));
//		photo = photoDao.save(photo);
		Favorite f = photo.getFavorites().iterator().next();
		if(f != null) {
			photo.getFavorites().remove(f);
		}
	}
	
	@Test
	public void testgetUserPhotoCountBytag() {
		User user = userDao.get(3L);
		String tag = "北京";
		Long count = photoDao.getUserPhotoCountBytag(user, tag);
		log.info(count);
		List<Photo> photos = photoDao.getUserPhotosByTag(user, tag);
		for(Photo photo : photos) {
			log.info(photo.getId());
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
		
}
