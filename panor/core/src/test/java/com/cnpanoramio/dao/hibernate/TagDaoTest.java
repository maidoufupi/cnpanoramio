package com.cnpanoramio.dao.hibernate;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.TagDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class TagDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserSettingsDao userSettingsDao;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TagDao tagDao;
	
	private Long userId;	
	
	@Before
	public void preMethodSetup() {
		userId = 1L;
	}

	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testGetOrCreateTag() {
		UserSettings userSettings = userSettingsDao.get(userId);
		String tagContent = "上海";
		Tag tag = tagDao.getOrCreateUserTag(userSettings, tagContent);
		log.info(tag.getId());
		log.info(tag);
	}
	
	@Test
	public void testGetUserTags() {
		UserSettings user = userSettingsDao.get(userId);
		Set<Tag> tags = user.getTags();
		
		log.info(tags.size());
		for(Tag tag : tags) {
			log.info(tag);
		}
	}
}
