package com.cnpanoramio.dao.hibernate;

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

import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class TravelDaoTest {

protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private TravelDao travelDao;
	@Autowired
	private UserSettingsDao userSettingsDao;
	
	private Long userId;
	
	
	@Before
	public void preMethodSetup() {
		userId = 1L;
	}

	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testGetByName() {
		UserSettings settings = userSettingsDao.get(userId);
		Travel t1 = new Travel("上海");
		t1.setUser(settings);
		settings.getTravels().add(t1);
		travelDao.save(t1);
		Travel travel = travelDao.getByName(userId, "上海");
		log.info(travel.getTitle());
		Assert.assertNotNull(travel);
	}
}
