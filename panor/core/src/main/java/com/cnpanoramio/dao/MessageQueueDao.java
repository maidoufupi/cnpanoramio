package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;

public interface MessageQueueDao extends GenericDao<MessageQueue, Long> {
	
	public MessageQueue persist(MessageQueue messageQueue);

	/**
	 * 获取（用户&Message对应的）Message queue
	 * 
	 * @param user
	 * @param message
	 * @return
	 */
	public MessageQueue getMessageQueue(User user, Message message);
	
	/**
	 * 获取用户的消息列表
	 * 
	 * @param user
	 * @return
	 */
	public List<MessageQueue> getMessageQueueList(User user, int pageSize, int pageNo);
	
	/**
	 * 获取Message对应的MessageQueue列表
	 * 
	 * @param user
	 * @param message
	 */
	public List<MessageQueue> getMessageQueueListByMessage(Message message);
	
}
