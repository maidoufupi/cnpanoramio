package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.domain.Comment.CommentType;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.CommentResponse.Comment;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.UserUtil;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

	private transient final Log log = LogFactory.getLog(CommentService.class);
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserSettingsManager userSettingsManager;
	@Autowired
	private PhotoManager photoManager;
	@Autowired
	private TravelDao travelDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private LikeDao likeDao;
	@Autowired
	private MessageManager messageManager;
	
	@Override
	public Comment save(User user, Comment comment) {
		
		user = userManager.get(user.getId());
		
		com.cnpanoramio.domain.Comment commentD = new com.cnpanoramio.domain.Comment();
		commentD.setContent(comment.getContent());
		commentD.setUser(user);
		
		if(comment.getType().equalsIgnoreCase("photo")) {
			Photo photo = photoManager.get(comment.getEntityId());
			commentD.setType(CommentType.photo);
			commentD.setPhoto(photo);
		}else if (comment.getType().equalsIgnoreCase("travel")) {
			Travel travel = travelDao.get(comment.getEntityId());
			commentD.setType(CommentType.travel);
			commentD.setTravel(travel);
		}else if (comment.getType().equalsIgnoreCase("comment")) {
			com.cnpanoramio.domain.Comment commentComment = commentDao.get(comment.getEntityId());
			commentD.setType(CommentType.comment);
			commentD.setComment(commentComment);
		}else if(comment.getType().equalsIgnoreCase("message")) {
			Message message = messageManager.get(comment.getEntityId());
			if(message.getType() == Message.MessageType.photo) {
				Photo photo = photoManager.get(message.getEntityId());
				commentD.setType(CommentType.photo);
				commentD.setPhoto(photo);
			}else if(message.getType() == Message.MessageType.travel) {
				Travel travel = travelDao.get(message.getEntityId());
				commentD.setType(CommentType.travel);
				commentD.setTravel(travel);
			}else if(message.getType() == Message.MessageType.message){
				comment.setEntityId(message.getEntityId());
				return this.save(user, comment);
			}
		}
		
		commentD = commentDao.persist(commentD);
		
		UserSettings settings = userSettingsManager.get(user);
		comment.setId(commentD.getId());
		// 昵称
		comment.setUsername(settings.getName());
		comment.setUserId(commentD.getUser().getId());
		// 头像
		if(null != settings.getAvatar()) {
			comment.setUserAvatar(settings.getAvatar().getId());
		}
		return comment;		
	}
	
	public boolean delete(Long id) {		
		
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Comment commentD = commentDao.get(id);
		if(commentD.getUser().getId() == me.getId()) {
			commentD.getPhoto().getComments().remove(commentD);
			commentDao.remove(commentD);
			return true;
		}
		
		return false;
	}

	public Collection<Comment> getComments(Long id, int pageSize, int pageNo, User user) {
		Photo photo = photoManager.get(id);
		List<com.cnpanoramio.domain.Comment> comments = commentDao.getComments(photo, pageSize, pageNo);
		
		List<Comment> cs = new ArrayList<Comment>();
		for(com.cnpanoramio.domain.Comment comment : comments) {
			Comment c1 = convertComment(comment, user);
			cs.add(c1);
		}
		
		return cs;
	}
	
	public Long getCount(Long id) {
		return commentDao.getCommentSize(id);
	}

	@Override
	public Comment modify(Long id, String content) {
		com.cnpanoramio.domain.Comment comment = commentDao.get(id);
		comment.setContent(content);
		return convertComment(comment, null);
	}
	
	@Override
	public Comment convertComment(com.cnpanoramio.domain.Comment commentD, User user) {
		Comment comment = new Comment();
		UserSettings settings = userSettingsManager.get(commentD.getUser());
		comment.setId(commentD.getId());
		if(null != settings.getAvatar()) {
			comment.setUserAvatar(settings.getAvatar().getId());
		}else {
			comment.setUserAvatar(1L);
		}
		
		// 昵称
		comment.setUsername(settings.getName());
		comment.setCreateDate(commentD.getCreateDate());
		comment.setUserId(commentD.getUser().getId());
		comment.setContent(commentD.getContent());
		if(null != commentD.getPhoto()) {
			comment.setEntityId(commentD.getPhoto().getId());
		}
		
		comment.setLikeCount(commentD.getLikes().size());
		
		// 设置此评论是否被此用户赞
		if(null != user) {
			Like like = likeDao.getComment(commentD, user);
			if(null != like) {
				comment.setLike(true);
			}
		}
		
		// 转换评论的评论
		if(null != commentD.getComments()) {
			comment.setComments(convertComments(commentD.getComments(), user));
		}
		return comment;
	}

	@Override
	public List<Comment> convertComments(
			List<com.cnpanoramio.domain.Comment> commentDs, User user) {
		List<Comment> comments = new ArrayList<Comment>(0);
		for(com.cnpanoramio.domain.Comment commentD : commentDs) {
			comments.add(this.convertComment(commentD, user));			
		}
		return comments;
	}

	@Override
	public List<Comment> getPhotoComments(Long photoId, User user) {
		Photo photo = photoManager.get(photoId);
		return convertComments(photo.getComments(), user);
	}
}
