package com.cnpanoramio.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoPanoramioDao;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class PhotoPanoramioDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private PhotoPanoramioDao photoPanoramioDao;
	@Autowired
	private PhotoDao photoDao;
	
	@Before
	public void preMethodSetup() {

	}

	@After
	public void postMethodTearDown() {

	}
	
	@Test
	public void testPersisted() {
		Photo photo = photoDao.get(1L);
		PhotoPanoramio pp = new PhotoPanoramio();
		pp.setUserId(photo.getOwner().getId());
		pp.setCreateDate(photo.getCreateDate());
	}
}
