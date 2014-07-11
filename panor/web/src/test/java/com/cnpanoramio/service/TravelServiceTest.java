package com.cnpanoramio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.json.TravelResponse.Travel;

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
public class TravelServiceTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private TravelService travelService;
	@Autowired
	private TravelManager travelManager;
	
	private User user;
	
	@Before
	public void preMethodSetup() {
		user = new User();
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
	public void testAddSpotPhotos() {
		List<Long> photoIds = new ArrayList<Long>();
		photoIds.add(5L);
		Travel travel = travelService.addSpotPhotos(1L, photoIds);
		travel = travelService.addSpotPhotos(2L, photoIds);
		
		// debug
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			log.debug("input param:" + ow.writeValueAsString(travel));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch() {
		com.cnpanoramio.domain.Travel tra = new com.cnpanoramio.domain.Travel();
		tra.setAddress("江苏省常州镇");
		tra.setTitle("江苏省常州");
		travelManager.save(tra);
		
		tra = new com.cnpanoramio.domain.Travel();
		tra.setAddress("江苏无锡");
		tra.setTitle("江苏");
		travelManager.save(tra);
		
		List<com.cnpanoramio.domain.Travel> travels = travelManager.search("江苏省", Travel.class);
		
		for(com.cnpanoramio.domain.Travel travel : travels) {
			log.info(travel.getTitle());
			log.info(travel.getAddress());
		}
		Assert.assertEquals(2, travels.size());
	}
}
