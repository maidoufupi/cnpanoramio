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

import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.domain.MessageQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class MessageQueueDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageQueueDao messageQueueDao;
	
	@Autowired
	private UserDao userDao;
	
	private Long userId;
	
	@Before
	public void preMethodSetup() {
		userId = 1L;
	}

	@After
	public void postMethodTearDown() {
		
	}

	@Test
	public void testGetMessage() {
		User user = userDao.get(userId);
		List<MessageQueue> queues = messageQueueDao.getMessageQueueList(user, 10, 1);
		log.info(queues.size());
	}
}
