package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Message;

public interface MessageDao extends GenericDao<Message, Long> {

	/**
	 * 获取某个图片对应的图片
	 * 
	 * @param id
	 * @return
	 */
	public Message getMessage(Message.MessageType type, Long id);
}
