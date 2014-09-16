package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.json.MessageResponse.Message;

public interface MessageService {

	/**
	 * 获取某个消息
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public Message get(User user, Long id);
	
	/**
	 * 获取用户的消息
	 * 
	 * @param user
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Message> getUserMessages(User user, int pageSize, int pageNo);
	
	/**
	 * 获取用户的动态消息列表
	 * 
	 * @param user
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Message> getMessages(User user, int pageSize, int pageNo);
	
	/**
	 * 分享消息
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public Message share(User user, Long id, String content);
}
