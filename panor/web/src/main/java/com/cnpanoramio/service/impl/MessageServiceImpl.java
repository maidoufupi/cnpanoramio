package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.json.MessageResponse.Message;
import com.cnpanoramio.domain.Message.MessageType;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessagePhotoManager;
import com.cnpanoramio.service.MessageService;
import com.cnpanoramio.service.MessageTravelManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private UserSettingsManager userSettingsManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private MessagePhotoManager messagePhotoManager;

	@Autowired
	private MessageTravelManager messageTravelManager;

	@Override
	public Message get(User user, Long id) {
		return transformMessage(user, messageManager.get(id));
	}

	@Override
	public List<Message> getUserMessages(User user, int pageSize, int pageNo) {
		return transformMessage(user, messageManager.getUserMessages(user, pageSize, pageNo));
	}

	@Override
	public List<Message> getMessages(User user, int pageSize, int pageNo) {
		return transformMessage(user, messageManager.getMessages(user, pageSize, pageNo));
	}
	
	@Override
	public Message share(User user, Long id, String content) {
		return transformMessage(user, messageManager.share(user, id, content));
	}

	private Message transformMessage(User user,
			com.cnpanoramio.domain.Message message) {
		if (message.getType() == MessageType.photo) {
			return messagePhotoManager.transformMessage(user, message);
		} else if (message.getType() == MessageType.travel) {
			return messageTravelManager.transformMessage(user, message);
		} else if (message.getType() == MessageType.message) {

			Message outMessage = new Message();
			outMessage.setId(message.getId());
			outMessage.setUser(UserUtil.getSimpleOpenInfo(userSettingsManager
					.get(message.getUser())));
			outMessage.setType(message.getType());
			outMessage.setCreateDate(message.getCreateDate());

			Message sharedMessage = this.get(user, message.getEntityId());

			// 图片描述设置为动态正文内容
			outMessage.setContent(message.getContent());

			outMessage.setShareMessage(sharedMessage);
			return outMessage;
		}
		return null;
	}

	private List<com.cnpanoramio.json.MessageResponse.Message> transformMessage(
			User user, List<com.cnpanoramio.domain.Message> messages) {
		List<Message> outMessages = new ArrayList<Message>(0);
		for (com.cnpanoramio.domain.Message message : messages) {
			outMessages.add(transformMessage(user, message));
		}
		return outMessages;
	}

	

}
