package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
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
public class PhotoServiceTest {
	
	protected transient final Log log = LogFactory.getLog(getClass());

	private PhotoService photoService;

	private InputStream ins;
	URL url;
	private Collection<Photo> photosForUser;

	@Before
	public void preMethodSetup() {
//		url = getClass().getResource("/image/IMAG1340.jpg");
		
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testPhotoStore() throws IOException, ImageReadException {
//		photoService.store(getMultipartBody());
	}

	@Test
	public void testFillPhotoDetail() throws ImageReadException, IOException {
		ins = getClass().getResourceAsStream("/image/image1.jpg");
		Photo photo = new Photo();
		photoService.fillPhotoDetail(ins, photo);
		log.info(photo.getDetails().getExifVersion());
		log.info(photo.getDetails().getMeteringMode());
		log.info(photo.getDetails().getMake());
		log.info(photo.getDetails().getModel());
		log.info(photo.getDetails().getExposureTime());
		log.info(photo.getDetails().getFocalLength());
		log.info(photo.getDetails().getFNumber());
		log.info(photo.getDetails().getISO());
				
//		log.info(photo.getGpsPoint().getLat());
//		log.info(photo.getGpsPoint().getLng());
//		log.info(photo.getGpsPoint().getAlt());
//		Assert.notNull(photo.getGpsPoint());
//		Assert.notNull(photo.getGpsPoint().getAlt());
	}
	
	@Test
	public void testNoDetails() throws ImageReadException, IOException {
		ins = getClass().getResourceAsStream("/image/image2.png");
		Photo photo = new Photo();
		photoService.fillPhotoDetail(ins, photo);
		log.info(photo.getDetails().getExifVersion());
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

	private MultipartBody getMultipartBody() {
		List<Attachment> atts = new LinkedList<Attachment>();
		atts.add(new Attachment("image", "application/octet-stream", getClass().getResourceAsStream("/image/IMAG1340.jpg")));
		return new MultipartBody(atts, true);
	}
}
