package com.cnpanoramio.service.impl;

import java.util.Iterator;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.MessageManager;

public abstract class AbstractMessageManagerImpl implements MessageManager {

	private UserManager userManager;
	
	private UserSettingsDao userSettingsDao;
	
	private MessageDao messageDao;
	
	private MessageQueueDao messageQueueDao;
	
	public void publishMessage(Long userId, Message message) {
		UserSettings user = userSettingsDao.get(userId);
		Iterator<User> iter = user.getFollower().iterator();
		while(iter.hasNext()) {
			User follower = iter.next();
			MessageQueue queue = messageQueueDao.getMessageQueue(follower, message);
			if(null == queue) {
				queue = new MessageQueue();
				queue.setUser(follower);
				queue.setMessage(message);
				queue = messageQueueDao.save(queue);
			}else {
				messageQueueDao.save(queue);
			}
		}
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserSettingsDao getUserSettingsDao() {
		return userSettingsDao;
	}

	@Autowired
	public void setUserSettingsDao(UserSettingsDao userSettingsDao) {
		this.userSettingsDao = userSettingsDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	@Autowired
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public MessageQueueDao getMessageQueueDao() {
		return messageQueueDao;
	}

	@Autowired
	public void setMessageQueueDao(MessageQueueDao messageQueueDao) {
		this.messageQueueDao = messageQueueDao;
	}
	
}
