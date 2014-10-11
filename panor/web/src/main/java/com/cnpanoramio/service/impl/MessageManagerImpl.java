package com.cnpanoramio.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.Message.MessageType;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessagePhotoManager;
import com.cnpanoramio.service.MessageQueueManager;
import com.cnpanoramio.service.MessageTravelManager;
import com.cnpanoramio.service.UserSettingsManager;

@Service
@Transactional
public class MessageManagerImpl extends GenericManagerImpl<Message, Long>
		implements MessageManager {

	private transient final Log log = LogFactory.getLog(getClass());

	private MessageDao messageDao;

	@Autowired
	private UserSettingsManager userSettingsManager;

	@Autowired
	private UserSettingsDao userSettingsDao;

	@Autowired
	private MessageQueueDao messageQueueDao;

	@Autowired
	private MessagePhotoManager messagePhotoManager;

	@Autowired
	private MessageTravelManager messageTravelManager;

	@Autowired
	private MessageQueueManager messageQueueManager;

	@Autowired
	private LikeManager likeManager;

	public MessageDao getMessageDao() {
		return messageDao;
	}

	@Autowired
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
		this.dao = messageDao;
	}
	
	@Override
	public Message getMessage(MessageType type, Long id) {
		return messageDao.getMessage(type, id);
	}

	@Override
	public com.cnpanoramio.json.MessageResponse.Message addOrUpdate(User user,
			Message.MessageType type,
			com.cnpanoramio.json.MessageResponse.Message inMessage) {

		log.debug("addOrUpdate message type " + inMessage.getType());

		Message message = null;
		if (Message.MessageType.photo == type) {
			message = messagePhotoManager.addOrUpdate(user, inMessage
					.getPhoto().getId());
			return messagePhotoManager.transformMessage(user, message);
		} else if (Message.MessageType.travel == type) {
			message = messageTravelManager.addOrUpdate(user, inMessage
					.getTravel().getId());
			return messageTravelManager.transformMessage(user, message);
		}

		return null;
	}

	@Override
	public void publishMessage(Long messageId) {

		log.debug("publish message " + messageId);

		Message message = messageDao.get(messageId);
		UserSettings user = userSettingsDao.get(message.getUser().getId());
		Iterator<User> iter = user.getFollower().iterator();
		while (iter.hasNext()) {
			messageQueueManager.addOrUpdate(iter.next(), message);
		}
	}

	@Override
	public void deleteMessage(Long userId, Long messageId) {
		Message message = messageDao.get(messageId);
		if (!message.getUser().getId().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		messageDao.remove(message);
	}

	@Override
	public void removeMessage(Long userId, Long messageId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Message> getUserMessages(User user, int pageSize, int pageNo) {
		return messageDao.getUserMessages(user, pageSize, pageNo);
	}

	@Override
	public List<Message> getMessages(User user, int pageSize, int pageNo) {
		return messageDao.getMessages(user, pageSize, pageNo);
	}

	@Override
	public void like(User user, Long id) {
		Message message = messageDao.get(id);
		if (message.getType() == Message.MessageType.photo) {
			likeManager.likePhoto(user, message.getEntityId());
		} else if (message.getType() == Message.MessageType.travel) {
			likeManager.likeTravel(user, message.getEntityId());
		} else if (message.getType() == Message.MessageType.message) {
			this.like(user, message.getEntityId());
		}
	}
	
	@Override
	public void unLike(User user, Long id) {
		Message message = messageDao.get(id);
		if (message.getType() == Message.MessageType.photo) {
			likeManager.unLikePhoto(user, message.getEntityId());
		} else if (message.getType() == Message.MessageType.travel) {
			likeManager.unLikeTravel(user, message.getEntityId());
		} else if (message.getType() == Message.MessageType.message) {
			this.unLike(user, message.getEntityId());
		}
	}

	@Override
	public Message share(User user, Long id, String content) {
		Message shareMessage = messageDao.get(id);
		Message message = new Message();
		message.setUser(user);
		message.setType(Message.MessageType.message);
		if (shareMessage.getType() == Message.MessageType.message) {
			// 如果信息为分享信息，则设置原始信息为分享的信息
			message.setEntityId(shareMessage.getEntityId());
		} else {
			// 如果信息不是分享信息，则设置此信息为分享信息
			message.setEntityId(shareMessage.getId());
		}

		message.setContent(content);
		message = getMessageDao().persist(message);
		return message;
	}

	

}
