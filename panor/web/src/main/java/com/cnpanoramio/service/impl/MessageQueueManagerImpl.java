package com.cnpanoramio.service.impl;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.service.MessageQueueManager;

public class MessageQueueManagerImpl implements MessageQueueManager {

	@Autowired
	private MessageQueueDao messageQueueDao;
	
	@Autowired
	private UserManager userManager;
	
	@Override
	public List<Message> getMessages(Long userId, int pageSize, int pageNo) {
		User user = userManager.get(userId);
		// TODO
		messageQueueDao.getMessageQueueList(user, pageSize, pageNo);
		return null;
	}

}
