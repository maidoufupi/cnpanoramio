package com.cnpanoramio.service;

import com.cnpanoramio.domain.Message;


public interface MessageManager {
	
	/**
	 * 新增或更新动态消息
	 * 
	 * @param userId
	 * @param id
	 */
	public void addOrUpdate(Long userId, Long id);
	
	/**
	 * 发布消息到follwer消息队列
	 * 
	 * @param userId
	 * @param message
	 */
	public void publishMessage(Long userId, Message message);

}
