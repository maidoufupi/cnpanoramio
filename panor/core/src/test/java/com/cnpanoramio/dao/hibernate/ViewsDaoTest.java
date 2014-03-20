package com.cnpanoramio.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Views;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml"
		})
@Transactional
public class ViewsDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ViewsDao viewsDao;	
	@Autowired
	private PhotoDao photoDao;	
	
	private String appId = "my_app";
	private String appId_2 = "my_app2";
	
	@Before
	public void preMethodSetup() {
	}

	@After
	public void postMethodTearDown() {
	}
	
	@Test
	public void testPersisted() {
		Views view = new Views();
		view.setPk(new Views.ViewsPK(1L, appId));
		view = viewsDao.save(view);
		log.info(view.getPk().getDate());
		Assert.assertTrue(1L == view.getPk().getPhotoId());
		
		viewsDao.view(1L, appId);
		viewsDao.view(1L, appId);
		viewsDao.view(1L, appId);
		viewsDao.view(1L, appId);
		
		Photo photo = photoDao.get(1L);
		Assert.assertTrue(1 == photo.getViews().size());
		
		viewsDao.view(1L, appId_2);
		viewsDao.view(1L, appId_2);
		photo = photoDao.get(1L);
		log.info(photo.getViews().size());
		Assert.assertTrue(2 == photo.getViews().size());
		
		photo.getViews().add(new Views(photo.getId(), appId));
		photo = photoDao.save(photo);
		Assert.assertTrue(1 == photo.getViews().size());		
	}
	
	@Test
	public void testGetViewsCount() {
		Long photoId = 1L;
		viewsDao.view(photoId, appId);
		viewsDao.view(photoId, appId_2);
		viewsDao.view(photoId, appId_2);
		int count = viewsDao.getViewsCount(photoId, appId_2);
		Assert.assertEquals(2, count);
		count = viewsDao.getViewsCount(photoId);
		Assert.assertEquals(3, count);
	}
	
	@Test
	public void testGetViewsList() {
		Long photoId = 1L;
		
		viewsDao.view(photoId, appId);
		viewsDao.view(photoId, appId_2);
		viewsDao.view(photoId, appId_2);
		
		List<Views> views = viewsDao.getViewsList(photoId);
		Assert.assertEquals(2, views.size());
		views = viewsDao.getViewsList(photoId, appId_2);
		Assert.assertEquals(1, views.size());
	}
	
	@Test
	public void testGetViews() {
		Date date = Calendar.getInstance().getTime();
		Long photoId = 1L;
		
		viewsDao.view(photoId, appId);
		viewsDao.view(photoId, appId_2);
		viewsDao.view(photoId, appId_2);
		
		int count = viewsDao.getViewsCount(photoId, date);
		Assert.assertEquals(3, count);
		Views view = viewsDao.getViews(photoId, appId_2, date);
		Assert.assertEquals(2, view.getCount());
		
		List<Views> views = viewsDao.getViewsList(photoId, date);
		Assert.assertEquals(2, views.size());
	}

	public ViewsDao getViewsDao() {
		return viewsDao;
	}

	public void setViewsDao(ViewsDao viewsDao) {
		this.viewsDao = viewsDao;
	}
	
	
}
