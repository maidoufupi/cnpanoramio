package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.cnpanoramio.domain.Point;

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
public class GPSRestServiceTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private GPSRestService gpsService;
	
	@Before
	public void preMethodSetup() {
		
	}

	@After
	public void postMethodTearDown() {
	}
	
	@Test
	public void testConvert() {
		Point p = gpsService.convert("34.836669916666665", "116.39122772222223", "gps", "mars");
		
		log.info(p.getLat());
		log.info(p.getLng());
	}
}
