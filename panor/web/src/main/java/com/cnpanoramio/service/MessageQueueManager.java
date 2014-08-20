package com.cnpanoramio.service;

import java.util.List;

import com.cnpanoramio.domain.Message;

public interface MessageQueueManager {

	/**
	 * 获取用户的消息列表
	 * 
	 * @param userId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Message> getMessages(Long userId, int pageSize, int pageNo);
}
