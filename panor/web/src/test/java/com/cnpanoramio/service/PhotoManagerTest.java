package com.cnpanoramio.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
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
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml"
		})
public class PhotoManagerTest {
	protected transient final Log log = LogFactory.getLog(getClass());
	
	private PhotoService photoService;
	
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
		
		photoService.store(new MultipartBody(true));
		
	}
	
	@Test
	public void testFillPhotoDetail() throws ImageReadException, IOException {
		Photo photo = new Photo();
		photoService.fillPhotoDetail(ins, photo);
		log.info(photo.getGpsPoint().getGeoLat());
		log.info(photo.getGpsPoint().getGeoLong());
		log.info(photo.getGpsPoint().getGeoAlti());
		Assert.notNull(photo.getGpsPoint());
		Assert.notNull(photo.getGpsPoint().getGeoAlti());
	}
	
	@Test
	public void testGetPhotosForUser() {
		photosForUser = photoService.getPhotosForUser("admin");
		Assert.isTrue(photosForUser.isEmpty());
	}
	
	public PhotoService getPhotoService() {
		return photoService;
	}
	
	@Autowired
	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
		
}
