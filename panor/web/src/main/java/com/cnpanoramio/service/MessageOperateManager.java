package com.cnpanoramio.service;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Message;

public interface MessageOperateManager {

	public Message addOrUpdate(User user, Long id);
	
	public com.cnpanoramio.json.MessageResponse.Message transformMessage(User user, Message message);
}
