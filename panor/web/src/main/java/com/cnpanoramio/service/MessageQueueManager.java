package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;

public interface MessageQueueManager {
	
	public MessageQueue addOrUpdate(User user, Message message);

	/**
	 * 获取用户的消息列表 分页
	 * 
	 * @param userId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<com.cnpanoramio.json.MessageResponse.Message> getMessages(User user, int pageSize, int pageNo);
	
	/**
	 * 从某人的消息列表中移除某个消息
	 * 
	 * @param userId
	 * @param message
	 */
	public void removeMessage(Long userId, Message message);
	
	/**
	 * 从所有人的消息列表中移除某个消息
	 * 
	 * @param userId
	 * @param message
	 */
	public void removeMessage(Message message);
}
