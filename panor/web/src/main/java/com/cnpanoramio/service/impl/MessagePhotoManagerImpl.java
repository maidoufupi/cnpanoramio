package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessagePhotoManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class MessagePhotoManagerImpl extends AbstractMessageManagerImpl implements MessagePhotoManager {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeManager likeManager;
	
	@Override
	public Message addOrUpdate(User user, Long id) {
		
		log.debug("addOrUpdate photo message " + id);
		
		user = this.getUser(user.getId());
		
		Photo photo = photoManager.get(id);
		// AUTHCHECK 权限检查 - 图片是否属于此用户
		if(!photo.getOwner().equals(user)) {
			throw new AccessDeniedException("Access Denied! Photo id: "
					+ photo.getId() + ", User id: " + user.getId());
		}
		
		Message message = getMessageDao().getMessage(Message.MessageType.photo, id);
		if(null == message) {
			message = new Message();
			message.setUser(user);
			message.setType(Message.MessageType.photo);
			message.setEntityId(id);
			message = getMessageDao().persist(message);
			log.debug("add photo message " + message.getId());
		}else {
			log.debug("update photo message " + message.getId());
			getMessageDao().save(message);
		}
		messageManager.publishMessage(message.getId());
		return message;
	}

	@Override
	public com.cnpanoramio.json.MessageResponse.Message transformMessage(User user,	Message message) {
		com.cnpanoramio.json.MessageResponse.Message outMessage = new com.cnpanoramio.json.MessageResponse.Message();
		outMessage.setId(message.getId());
		outMessage.setUser(UserUtil.getSimpleOpenInfo(getUserSettingsManager().get(message.getUser())));
		outMessage.setType(message.getType());
		outMessage.setCreateDate(message.getCreateDate());
		
		Photo photo = photoManager.get(message.getEntityId());
		// 图片描述设置为动态正文内容
		outMessage.setContent(photo.getDescription());
		// 图片标签设置为动态的标签
		List<String> tags = new ArrayList<String>(0);
		for(Tag tag : photo.getTags()) {
			tags.add(tag.getContent());
		}
		outMessage.setTags(tags);
		// 设置图片的评论为动态的评论
		outMessage.setComments(commentService.convertComments(photo.getComments(), null));
		
		// 设置此图片为动态主图片
		outMessage.setPhoto(photoService.getPhotoProperties(message.getEntityId(), user));
		
		// 多少个赞
		outMessage.setLikeCount(photo.getLikes().size());
		
		// 是否被用户赞
		outMessage.setLike(outMessage.getPhoto().getLike());
//		if(null != user) {
//			Like like = likeManager.getLikePhoto(user, photo);
//			if(null != like) {
//				outMessage.setLike(true);
//			}
//		}
		
		return outMessage;
	}
	
}
