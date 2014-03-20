package com.cnpanoramio.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
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
public class PhotoPanoramioIndexServiceTest {
	
	protected transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PhotoPanoramioIndexService panoramioIndexService;

	@Before
	public void preMethodSetup() {
	}

	@After
	public void postMethodTearDown() {
	}
	
	@Test
	public void testGetPhotoPanorIndex() {

		Double swLatD = Double.parseDouble("33");
		Double swLngD = Double.parseDouble("110");
		Double neLatD = Double.parseDouble("36");
		Double neLngD = Double.parseDouble("133");
		int levelI = Integer.parseInt("2");
		int widthI = Integer.parseInt("100");
		int heightI = Integer.parseInt("100");
		
		MapVendor mVendor = MapVendor.gaode;
		
		List<PhotoPanoramio> photos = panoramioIndexService.getPanoramio(new Point(swLatD, swLngD), 
				new Point(neLatD, neLngD), levelI, mVendor, widthI, heightI);
		log.info(photos.size());
		Assert.assertTrue(photos.size() == 2);
	}

	@Test
	public void testPhotoPanorUpdate(){
		panoramioIndexService.updatePanoramioIndex();		
	}

}
