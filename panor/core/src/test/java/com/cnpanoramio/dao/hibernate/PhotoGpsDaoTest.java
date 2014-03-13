package com.cnpanoramio.dao.hibernate;

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
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class PhotoGpsDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private PhotoGpsDao photoGpsDao;
	
	
	@Before
	public void preMethodSetup() {
		
	}

	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testPersisted() {
		Long photoId = 5L;
		PhotoGps gps = new PhotoGps();
		gps.setPk(new PhotoGps.PhotoGpsPK(photoId, MapVendor.gaode));
		gps.setGps(new Point(33D, 123D));
		photoGpsDao.save(gps);
		
		PhotoGps out = photoGpsDao.get(new PhotoGps.PhotoGpsPK(photoId, MapVendor.gaode));
		Assert.assertTrue(out.getPk().getPhotoId()== photoId);
		Assert.assertTrue(out.getGps().getLat() == 33D);
	}
}
