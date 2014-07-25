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
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoLatestIndex;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
import com.cnpanoramio.domain.Point;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" // "classpath*:/applicationContext-resources.xml",
})
@Transactional
public class PhotoPanoramioIndexDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PhotoPanoramioIndexDao photoPanoramioIndexDao;
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
		PhotoPanoramioIndex index = new PhotoPanoramioIndex();
		index.setPk(new PhotoPanoramioIndexPK(1, 36D, 123D));
		index.setPhoto(photo);
		photoPanoramioIndexDao.save(index);

		PhotoPanoramioIndex out = photoPanoramioIndexDao
				.get(new PhotoPanoramioIndexPK(1, 36D, 123D));
		Assert.assertTrue(out.getPhoto().getId() == photoId);
	}

	@Test
	public void testGetPhotoPanoramio() {
		List<Photo> photos = photoPanoramioIndexDao.getPhotoPanoramio(
				29.964453D, 120.352478D, 31.805227D, 121.912537D, 9, MapVendor.gaode,
				387, 781);
		Assert.assertEquals(2, photos.size());
		for (Photo photo : photos) {
			log.info(photo.getId());
		}
	}

	@Test
	public void testUpdatePhotoPanoramio() {
		photoPanoramioIndexDao.updatePhotoPanoramioIndex();
		List<Photo> photos = photoPanoramioIndexDao.getPhotoPanoramio(
				34D, 110D, 36D, 126D, 2, MapVendor.gaode,
				100, 100);
		Assert.assertTrue(photos.size() == 2);
		for (Photo photo : photos) {
			log.info(photo.getId());
		}
	}
	
	@Test
	public void testGetLatestPanoramio() {
		List<Photo> photos = photoPanoramioIndexDao.getLatestPanoramio(23D, 110D, 40D, 130D, 2, MapVendor.gaode, 100, 100);
		Assert.assertEquals(2, photos.size());
	}
	
	@Test
	public void testGetUserPanoramio() {
		List<Photo> photos = photoPanoramioIndexDao.getUserPhotoPanoramio(23D, 110D, 40D, 130D, 2, MapVendor.gaode, 100, 100, 1L, false);
		Assert.assertEquals(2, photos.size());
		for(Photo photo : photos) {
			log.info(photo.getId());
		}
	}
	
	@Test
	public void testGetUserFavPanoramio() {
		List<Photo> photos = photoPanoramioIndexDao.getUserPhotoPanoramio(23D, 110D, 40D, 130D, 2, MapVendor.gaode, 100, 100, 1L, true);
		Assert.assertEquals(2, photos.size());
		for(Photo photo : photos) {
			log.info(photo.getId());
		}
	}
	
	@Test
	public void testSearchPhoto() {
		Double swLat = 23D;
		Double swLng = 90D;
		Double neLat = 40D;
		Double neLng = 130D;
		int level = 19;
		int width = 1000;
		int height = 1000;
		String term = "大  理";
		String type = "all";
		
		List<Photo> photos = photoPanoramioIndexDao.search(swLat, swLng, neLat, neLng, level, width, height, term, type);
		log.info("search all: ");
		for(Photo photo : photos) {
			log.info("  " + photo.getId());
			log.info("  " + photo.getName());
			log.info("  " + photo.getDescription());
		}
		
		type = "photo";
		photos = photoPanoramioIndexDao.search(swLat, swLng, neLat, neLng, level, width, height, term, type);
		log.info("search photo: ");
		for(Photo photo : photos) {
			log.info("  " + photo.getId());
			log.info("  " + photo.getName());
			log.info("  " + photo.getDescription());
		}
		
		term = "山东";
		type = "travel";
		photos = photoPanoramioIndexDao.search(swLat, swLng, neLat, neLng, level, width, height, term, type);
		log.info("search travel: ");
		for(Photo photo : photos) {
			log.info("  " + photo.getId());
			log.info("  " + photo.getName());
			log.info("  " + photo.getDescription());
		}
	}
}
