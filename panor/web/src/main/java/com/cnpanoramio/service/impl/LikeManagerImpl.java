package com.cnpanoramio.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class LikeManagerImpl implements LikeManager {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private TravelDao travelDao;
	
	@Autowired
	private LikeDao likeDao;
	
	@Autowired
	private PhotoDao photoDao;
	
	@Autowired
	private UserManager userManager = null;
	
	@Override
	public Like likePhoto(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		Photo photo = photoDao.get(id);
		
		Like like = likeDao.getPhoto(photo, me);
		if(null == like) {
			like = likeDao.likePhoto(photo, me);
			photo.getLikes().add(like);
		}else {
			likeDao.unLikePhoto(photo, me);
			photo.getLikes().remove(like);
		}
		return like;
	}

	@Override
	public Like likeComment(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		Comment comment = commentDao.get(id);
		Like like = likeDao.getComment(comment, me);
		if(null == like) {
			like = likeDao.likeComment(comment, me);
			comment.getLikes().add(like);
		}else {
			likeDao.unLikeComment(comment, me);
			comment.getLikes().remove(like);
		}
		return like;
	}

	@Override
	public Like likeTravel(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		Travel travel = travelDao.get(id);
		Like like = likeDao.getTravel(travel, me);
		if(null == like) {
			like = likeDao.likeTravel(travel, me);
			travel.getLikes().add(like);
		}else {
			likeDao.unLikeTravel(travel, me);
			travel.getLikes().remove(like);
		}
		return like;
	}

}
