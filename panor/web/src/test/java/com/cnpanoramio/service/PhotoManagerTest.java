package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.utils.PhotoUtil;

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
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	
	private InputStream ins;
	URL url;
	private Collection<Photo> photosForUser;
	
	@Before
	public void preMethodSetup() {
//		ins = getClass().getResourceAsStream("/image/photo.jpg");
		// 测试不同设备拍出来的图片
		ins = getClass().getResourceAsStream("/image/IMG_2401.JPG");
		User user = new User();
		user.addRole(roleManager.getRole(Constants.USER_ROLE));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", "user", user.getAuthorities());
        auth.setDetails(user);
		SecurityContextHolder.getContext().setAuthentication(auth);
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
		User user = userManager.get(1L);
		photo.setOwner(user);
		photoManager.fillPhotoDetail(ins, photo);
		PhotoDetails details = photo.getDetails();
		/* Camera */
		log.info(details.getMake());
		log.info(details.getModel());
		log.info(details.getExposureTime());
		/* Origin */
		log.info(details.getDateTime());
		log.info(details.getDateTimeDigitized());
		log.info(details.getDateTimeOriginal());
		/* Advanced photo */
		log.info(details.getExposureProgram());
		
		/* GPS */
		log.info(details.getGPSProcessingMethod());
		log.info(details.getGPSDateStamp());
		log.info(photo.getGpsPoint().getLat());
		log.info(photo.getGpsPoint().getLng());
		log.info(photo.getGpsPoint().getAlt());
		
		PhotoCameraInfo cameraInfo = PhotoUtil.transformCameraInfo(photo);
		log.info(cameraInfo.getExposureTime());
		log.info(cameraInfo.getFocalLength());
		log.info(cameraInfo.getFNumber());
		log.info(cameraInfo.getISO());
		log.info(cameraInfo.getExposureBias());
		log.info(cameraInfo.getFlash());
		
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
	
	@Test
	public void testUpload() throws IOException {
		
		MockMultipartFile file = new MockMultipartFile("test.jpg", "photo.jpg", "jpg", ins);
		log.info(file.getName());
		log.info(file.getOriginalFilename());
		PhotoProperties pp = photoManager.upload("", "", "", null, file);
		Photo photo = photoManager.getPhoto(pp.getId());
		Assert.notNull(photo.getDetails());
		Assert.isTrue(photo.getDetails().getGPSLatitude() != 0);
		log.info(photo.getDetails().getGPSLatitude());
		log.info(photo.getGpsPoint().getLat());
		Assert.isTrue(photo.getDetails().getGPSLatitude().equals(photo.getGpsPoint().getLat()));
		Assert.notNull(photo.getGpsPoint());
	}
}
