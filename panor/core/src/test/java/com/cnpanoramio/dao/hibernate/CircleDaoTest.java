package com.cnpanoramio.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CircleDao;
import com.cnpanoramio.domain.Circle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class CircleDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private CircleDao circleDao;
	
	@Autowired
	private UserDao userDao;
	
	private Long circleId;
	private Long userId;
	
	@Before
	public void preMethodSetup() {
		circleId = 1L;
		userId = 1L;
	}
	
	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testGetCircle() {
		Circle circle = circleDao.get(circleId);
		log.info(circle.getName());
		log.info(circle.getOwner().getUsername());
	}
	
	@Test
	public void testGetFollowSuggested() {
		User user = userDao.get(userId);
//		List<Long> users = circleDao.getFollowSuggested(user);
		List<User> users = circleDao.getFollowSuggested(user, 10, 1);
		
		for(User u : users) {
			log.info(u.getUsername());
		}
//		for(Long u : users) {
//			log.info(u);
//		}
	}
}
