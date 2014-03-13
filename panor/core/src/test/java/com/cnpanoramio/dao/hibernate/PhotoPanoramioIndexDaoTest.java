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
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" // "classpath*:/applicationContext-resources.xml",
})
@Transactional
public class PhotoPanoramioIndexDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PhotoPanoramioIndexDao photoPanoramioIndexDao;

	@Before
	public void preMethodSetup() {

	}

	@After
	public void postMethodTearDown() {

	}

	@Test
	public void testPersisted() {
		Long photoId = 5L;
		PhotoPanoramioIndex index = new PhotoPanoramioIndex();
		index.setPk(new PhotoPanoramioIndex.PhotoPanoramioIndexPK(1, 36D, 123D));
		index.setPhotoId(photoId);
		photoPanoramioIndexDao.save(index);

		PhotoPanoramioIndex out = photoPanoramioIndexDao
				.get(new PhotoPanoramioIndex.PhotoPanoramioIndexPK(1, 36D, 123D));
		Assert.assertTrue(out.getPhotoId() == photoId);
	}

	@Test
	public void testGetPhotoPanoramio() {
		List<PhotoPanoramio> photos = photoPanoramioIndexDao.getPhotoPanoramio(
				new Point(34D, 110D), new Point(36D, 126D), 2, MapVendor.gaode,
				100, 100);
		Assert.assertTrue(photos.size() == 2);
		for (PhotoPanoramio photo : photos) {
			log.info(photo.getPhotoId());
		}
	}

	@Test
	public void testUpdatePhotoPanoramio() {
		photoPanoramioIndexDao.updatePhotoPanoramioIndex();
		List<PhotoPanoramio> photos = photoPanoramioIndexDao.getPhotoPanoramio(
				new Point(34D, 110D), new Point(36D, 126D), 2, MapVendor.gaode,
				100, 100);
		Assert.assertTrue(photos.size() == 2);
		for (PhotoPanoramio photo : photos) {
			log.info(photo.getPhotoId());
		}
	}
}
