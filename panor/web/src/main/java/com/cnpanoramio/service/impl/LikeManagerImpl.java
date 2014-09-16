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
	public Like likePhoto(User user, Long id) {
		Photo photo = photoDao.get(id);
		
		Like like = likeDao.getPhoto(photo, user);
		if(null == like) {
			like = likeDao.likePhoto(photo, user);
		}
		return like;
	}
	
	@Override
	public void unLikePhoto(User user, Long id) {
		Photo photo = photoDao.get(id);
		
		Like like = likeDao.getPhoto(photo, user);
		if(null != like) {
			likeDao.remove(like);
		}
	}

	@Override
	public Like likeComment(User user, Long id) {
		Comment comment = commentDao.get(id);
		Like like = likeDao.getComment(comment, user);
		if(null == like) {
			like = likeDao.likeComment(comment, user);
		}
		return like;
	}
	
	@Override
	public void unLikeComment(User user, Long id) {
		Comment comment = commentDao.get(id);
		Like like = likeDao.getComment(comment, user);
		if(null != like) {
			likeDao.remove(like);
		}		
	}

	@Override
	public Like likeTravel(User user, Long id) {
		Travel travel = travelDao.get(id);
		Like like = likeDao.getTravel(travel, user);
		if(null == like) {
			like = likeDao.likeTravel(travel, user);
		}
		return like;
	}
	
	@Override
	public void unLikeTravel(User user, Long id) {
		Travel travel = travelDao.get(id);
		Like like = likeDao.getTravel(travel, user);
		if(null != like) {
			likeDao.remove(like);
		}		
	}

	@Override
	public Like getLikePhoto(User user, Photo photo) {
		return likeDao.getPhoto(photo, user);
	}

	@Override
	public Like getLikeComment(User user, Comment comment) {
		return likeDao.getComment(comment, user);
	}

	@Override
	public Like getLikeTravel(User user, Travel travel) {
		return likeDao.getTravel(travel, user);
	}

}
