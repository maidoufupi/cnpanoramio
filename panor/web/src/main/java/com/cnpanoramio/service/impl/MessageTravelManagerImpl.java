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
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessageTravelManager;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class MessageTravelManagerImpl extends AbstractMessageManagerImpl implements MessageTravelManager {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private TravelService travelService;
	
	@Autowired
	private TravelManager travelManager;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeManager likeManager;
	
	@Override
	public Message addOrUpdate(User user, Long id) {
		
		user = this.getUser(user.getId());
		
		Travel travel = travelManager.get(id);
		// AUTHCHECK 权限检查 - 旅行是否属于此用户
		if(!user.getId().equals(travel.getUser().getId())) {
			throw new AccessDeniedException("Access Denied! Travel id: "
					+ travel.getId() + ", User id: " + user.getId());
		}
		Message message = getMessageDao().getMessage(Message.MessageType.travel, id);
		if(null == message) {
			message = new Message();
			message.setUser(user);
			message.setType(Message.MessageType.travel);
			message.setEntityId(id);
			message = getMessageDao().persist(message);
			log.debug("add travel message " + message.getId());
		}else {
			log.debug("update travel message " + message.getId());
			getMessageDao().save(message);
		}
		
		messageManager.publishMessage(message.getId());
		
		return message;
	}

	@Override
	public com.cnpanoramio.json.MessageResponse.Message transformMessage(User user, Message message) {
		com.cnpanoramio.json.MessageResponse.Message outMessage = new com.cnpanoramio.json.MessageResponse.Message();
		outMessage.setId(message.getId());
		outMessage.setUser(UserUtil.getSimpleOpenInfo(getUserSettingsManager().get(message.getUser())));
		outMessage.setType(message.getType());
		outMessage.setCreateDate(message.getCreateDate());
		
		Travel travel = travelManager.getTravel(message.getEntityId());
		// 旅行描述设置为动态正文内容
		outMessage.setContent(travel.getDescription());
		// 旅行标签设置为动态的标签
		List<String> tags = new ArrayList<String>(0);
		
		for(Tag tag : travel.getTags()) {
			tags.add(tag.getContent());
		}
		outMessage.setTags(tags);
		// 设置旅行的评论为动态的评论
		outMessage.setComments(commentService.convertComments(travel.getComments(), null));
		
		// 设置此图片为动态主旅行
		outMessage.setTravel(travelService.getTravel(message.getEntityId()));
		
		// 多少个赞
		outMessage.setLikeCount(travel.getLikes().size());
				
		// 是否被用户赞
		if(null != user) {
			Like like = likeManager.getLikeTravel(user, travel);
			if(null != like) {
				outMessage.setLike(true);
			}
		}
		
		return outMessage;
	}

}
