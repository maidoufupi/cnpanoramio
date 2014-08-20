package com.cnpanoramio.dao.hibernate;

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
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGps;
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
	@Autowired
	private PhotoDao photoDao;
	
	@Before
	public void preMethodSetup() {
	}

	@After
	public void postMethodTearDown() {
	}
	
	@Test
	public void testPersisted() {
		Long photoId = 5L;
		Photo photo = photoDao.get(photoId);
		PhotoGps gps = new PhotoGps(photo, MapVendor.gaode, new Point(33D, 123D));
		gps = photoGpsDao.save(gps);
		photo.getGps().put(MapVendor.gaode, gps);

		Assert.assertTrue(photo.getGps().get(MapVendor.gaode).getPoint().getLat() == 33D);
	}
	
	@Test
	public void testGetPhotoGps() {
		Long photoId = 72L;
		Photo photo = photoDao.get(photoId);
		
		Assert.assertNotNull(photo.getGps().get(MapVendor.baidu).getPoint());
		log.info(photo.getGps().get(MapVendor.baidu).getPoint().getLat());
		log.info(photo.getGps().get(MapVendor.baidu).getPoint().getLng());
		log.info(photo.getGps().get(MapVendor.baidu).getPoint().getAddress());
	}
	
	@Test
	public void testGetAllByPhotoId() {
		Long photoId = 1L;
		List<PhotoGps> gps = photoGpsDao.getAll(photoId);
		Assert.assertEquals(1, gps.size());
	}
}
