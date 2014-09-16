package com.cnpanoramio.service.impl;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.service.MessageOperateManager;
import com.cnpanoramio.service.MessageQueueManager;
import com.cnpanoramio.service.UserSettingsManager;

public abstract class AbstractMessageManagerImpl implements MessageOperateManager {

	private UserManager userManager;
	
	private UserSettingsDao userSettingsDao;
	
	private UserSettingsManager userSettingsManager;
	
	private MessageDao messageDao;
	
	private MessageQueueDao messageQueueDao;
	
	private MessageQueueManager messageQueueManager;
	
	protected User getUser(Long id) {
		return getUserManager().get(id);
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

	public MessageQueueManager getMessageQueueManager() {
		return messageQueueManager;
	}

	@Autowired
	public void setMessageQueueManager(MessageQueueManager messageQueueManager) {
		this.messageQueueManager = messageQueueManager;
	}

	public UserSettingsManager getUserSettingsManager() {
		return userSettingsManager;
	}

	@Autowired
	public void setUserSettingsManager(UserSettingsManager userSettingsManager) {
		this.userSettingsManager = userSettingsManager;
	}
	
}
