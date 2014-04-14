package com.cnpanoramio.service;

import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserResponse;

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
	private UserSettingsManager userSettingsService = null;
	
	@Before
	public void preMethodSetup() {
		
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testGetUserSettings() throws IOException, ImageReadException {
		UserResponse.Settings settings = userSettingsService.getCurrentUserSettings();
		Assert.assertNull(settings);
	}
	
	@Test
	public void testGetUser() {
		User user = userSettingsService.getUser("matt_raible@yahoo.com");
		Assert.assertNotNull(user);
		Assert.assertTrue(user.getUsername().equals("user"));
	}
}
