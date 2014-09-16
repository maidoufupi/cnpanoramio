package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;
import com.cnpanoramio.service.MessageQueueManager;
import com.cnpanoramio.service.MessageService;

@Service
@Transactional
public class MessageQueueManagerImpl implements MessageQueueManager {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageQueueDao messageQueueDao;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private MessageService messageService; 
	
	@Override
	public MessageQueue addOrUpdate(User user, Message message) {
		MessageQueue queue = messageQueueDao.getMessageQueue(user, message);
		if(null == queue) {
			queue = new MessageQueue();
			queue.setUser(user);
			queue.setMessage(message);
			queue = messageQueueDao.persist(queue);
			log.debug("add message queue " + queue.getId());
		}else {
			log.debug("update message queue " + queue.getId());
			messageQueueDao.save(queue);
		}
		return queue;
	}
	
	@Override
	public List<com.cnpanoramio.json.MessageResponse.Message> getMessages(User user, int pageSize, int pageNo) {
		List<MessageQueue> queues = messageQueueDao.getMessageQueueList(user, pageSize, pageNo);
		List<com.cnpanoramio.json.MessageResponse.Message> messages 
			= new ArrayList<com.cnpanoramio.json.MessageResponse.Message>(0);
		for(MessageQueue queue : queues) {
			messages.add(messageService.get(user, queue.getMessage().getId()));
		}
		return messages;
	}

	@Override
	public void removeMessage(Long userId, Message message) {
		User user = userManager.get(userId);
		MessageQueue queue = messageQueueDao.getMessageQueue(user, message);
		queue.setDeleted(true);
	}

	@Override
	public void removeMessage(Message message) {
		List<MessageQueue> queues = messageQueueDao.getMessageQueueListByMessage(message);
		for(MessageQueue queue : queues) {
			queue.setDeleted(true);
		}
	}

	

}
