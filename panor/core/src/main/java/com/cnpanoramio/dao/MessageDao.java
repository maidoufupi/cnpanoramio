package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Message;

public interface MessageDao extends GenericDao<Message, Long> {

	public Message persist(Message message);
	
	/**
	 * 获取某个图片对应的图片
	 * 
	 * @param id
	 * @return
	 */
	public Message getMessage(Message.MessageType type, Long id);
	
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
	 * 获取用户关注的消息
	 * 
	 * @param user
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Message> getMessages(User user, int pageSize, int pageNo);
}
