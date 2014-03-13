package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.cnpanoramio.domain.Photo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = true,
        locations = { 
				"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-service.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "classpath*:/applicationContext-test.xml",
                "/WEB-INF/spring-security.xml",
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"})
public class PhotoManagerTest {
	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private PhotoManager photoManager;
	
	private InputStream ins;
	URL url;
	private Collection<Photo> photosForUser;
	
	@Before
	public void preMethodSetup() {
		url = getClass().getResource("/image/IMAG1340.jpg");
		ins = getClass().getResourceAsStream("/image/IMAG1340.jpg");
	}
	
	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testPhotoStore() throws IOException, ImageReadException {
		
//		photoService.store(new MultipartBody(true));
		
	}
	
	@Test
	public void testFillPhotoDetail() throws ImageReadException, IOException {
		Photo photo = new Photo();
		photoManager.fillPhotoDetail(ins, photo);
		log.info(photo.getGpsPoint().getLat());
		log.info(photo.getGpsPoint().getLng());
		log.info(photo.getGpsPoint().getAlt());
		Assert.notNull(photo.getGpsPoint());
		Assert.notNull(photo.getGpsPoint().getAlt());
	}
	
	@Test
	public void testGetUserPhotos() {
		Collection<Photo> photos = photoManager.getPhotosForUser("1", 10, 1);
		Assert.isTrue(photos.size() == 0);
	}
	
	@Test
	public void testGetPhotosForUser() {
		photosForUser = photoManager.getPhotosForUser("admin");
		Assert.isTrue(photosForUser.isEmpty());
	}
}
