package com.cnpanoramio.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserSettings;
import com.cnpanoramio.json.UserSettingsResponse;

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
public class UserSettingsServiceTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager = null;
	
	@Autowired
	private UserSettingsManager userSettingsManager = null;
	
	@Autowired
	private UserSettingsService userSettingsService = null;
	
	private Long userId;
	
	@Before
	public void preMethodSetup() {
		userId = 1L;
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testGetUserSettings() {
		User user = userManager.get(userId);
		UserSettings settings = userSettingsService.getUserSettings(user);
		Assert.assertNull(settings);
	}
	
	@Test
	public void testGetUser() {
		User user = userSettingsManager.getUser("anypossible.w@foxmail.com");
		Assert.assertNotNull(user);
//		Assert.assertTrue(user.getUsername().equals("user"));
	}
	
	@Test
	public void testChangePassword() {
		User user = userManager.get(userId);
		UserSettingsResponse.Password password = new UserSettingsResponse.Password();
		password.setCurrentPassword("gaode");
		password.setPassword("user");
		password.setConfirmPassword("user");
		userSettingsService.changePassword(user, password);
	}
	
	@Test
	public void testGetOpenInfo() {
		User user = userManager.get(userId);
		
		UserOpenInfo openInfo = userSettingsManager.getOpenInfo(user, user);
		log.info(openInfo.getDescription());
	}
}
