package com.cnpanoramio.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.IndexPhotoDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.IndexPhoto;
import com.cnpanoramio.domain.Photo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class IndexPhotoDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private IndexPhotoDao indexPhotoDao;
	
	@Autowired
	private PhotoDao photoDao;
	
	private Photo photo;
	private IndexPhoto indexPhoto;
	private List<IndexPhoto> photos;
	
	@Before
	public void preMethodSetup() {
		photos = new ArrayList<IndexPhoto>();
	}
	
	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testSave() {
		indexPhoto = new IndexPhoto();
		photo = photoDao.get(1L);
		indexPhoto.setPhoto(photo);
		photos.add(indexPhoto);
		
		indexPhoto = new IndexPhoto();
		indexPhoto.setPhoto(photoDao.get(new Long(3)));
		photos.add(indexPhoto);
		
		indexPhoto = new IndexPhoto();
		indexPhoto.setPhoto(photoDao.get(new Long(4)));
		photos.add(indexPhoto);
		
		indexPhotoDao.save(photos);
		
		photos = indexPhotoDao.getAllDistinct();
		
		Assert.assertEquals(photos.size(), 3);
		Assert.assertEquals(photos.get(0).getPhoto().getDetails().getGPSLatitude(), 36.0);
		for(IndexPhoto photo : photos) {
			log.info(photo.getId());
			log.info(photo.getPhoto().getDetails().getGPSLatitude());
		}
		
	}
}
