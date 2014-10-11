package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

import com.cnpanoramio.domain.Message;


public interface MessageManager extends GenericManager<Message, Long> {
	
	/**
	 * 获取不同对象的消息
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public Message getMessage(Message.MessageType type, Long id);
	
	/**
	 * 新增或更新动态消息
	 * 
	 * @param userId
	 * @param id
	 */
	public com.cnpanoramio.json.MessageResponse.Message addOrUpdate(User user, Message.MessageType type,  com.cnpanoramio.json.MessageResponse.Message message);
	
	/**
	 * 发布消息到follwer消息队列
	 * 
	 * @param messageId
	 */
	public void publishMessage(Long messageId);
	
	/**
	 * 删除消息
	 * 
	 * @param userId
	 * @param messageId
	 */
	public void deleteMessage(Long userId, Long messageId);
	
	/**
	 * 从自己的动态列表中移除消息
	 * 
	 * @param userId
	 * @param messageId
	 */
	public void removeMessage(Long userId, Long messageId);
	
	/**
	 * 转换成json需要的数据对象格式
	 * 
	 * @param message
	 * @return
	 */
//	public com.cnpanoramio.json.MessageResponse.Message transformMessage(User user, Message message);
	
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
	 * 赞
	 * 
	 * @param user
	 * @param id
	 */
	public void like(User user, Long id);
	
	/**
	 * 赞
	 * 
	 * @param user
	 * @param id
	 */
	public void unLike(User user, Long id);
	
	/**
	 * 分享消息
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public Message share(User user, Long id, String content);

}
