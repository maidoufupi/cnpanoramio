package com.cnpanoramio.dao.hibernate;

import java.util.List;

import junit.framework.Assert;

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

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class MessageDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageDao messageDao;
	
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
		Long photoId = 3L;
		Message message = messageDao.getMessage(Message.MessageType.photo, photoId);
		Assert.assertNull(message);
		message = new Message();
		message.setUser(null);
		message.setType(Message.MessageType.photo);
		message.setEntityId(photoId);
//		message.setCreateDate(new Date());
//		message.setModifyDate(new Date());
		message = messageDao.persist(message);
		log.info(message.getId());
		message = messageDao.getMessage(Message.MessageType.photo, photoId);
	}
	
	@Test
	public void testGetMessages() {
		userId = 3L;
		User user = userDao.get(userId);
		List<Message> messages = messageDao.getMessages(user, 10, 1);
		
		for(Message message : messages) {
			log.info(message.getId());
			log.info(message.getType());
		}
	}
}
