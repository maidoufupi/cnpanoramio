package com.cnpanoramio.service.impl;

import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.service.MessageManager;

public class MessagePhotoImpl extends AbstractMessageManagerImpl implements MessageManager {

	@Autowired
	private PhotoDao photoDao;
	
	@Override
	public void addOrUpdate(Long userId, Long id) {
		User user = getUserManager().get(userId);
		Message message = getMessageDao().getMessage(Message.MessageType.photo, id);
		if(null == message) {
			message = new Message();
			message.setUser(user);
			message.setType(Message.MessageType.photo);
			message.setEntityId(id);
			message = getMessageDao().save(message);
		}else {
			getMessageDao().save(message);
		}
		publishMessage(userId, message);
	}

	public PhotoDao getPhotoDao() {
		return photoDao;
	}

	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}
	
	

}
