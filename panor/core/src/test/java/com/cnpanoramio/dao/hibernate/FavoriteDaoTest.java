package com.cnpanoramio.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
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

import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Views;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml"
		})
@Transactional
public class FavoriteDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private FavoriteDao favoriteDao;	
	@Autowired
	private PhotoDao photoDao;	
	
	@Autowired
	private UserDao userDao;
	
	private String appId = "my_app";
	private String appId_2 = "my_app2";
	
	@Before
	public void preMethodSetup() {
	}

	@After
	public void postMethodTearDown() {
	}	
	
	@Test
	public void testGetUserPhotoFavoriteCount() {
		User user = userDao.get(3L);
		Long views = favoriteDao.getUserPhotoFavoriteCount(user);
		log.info(views);
	}	

	
}
