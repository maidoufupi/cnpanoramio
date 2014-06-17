package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Favorite;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;

public interface LikeDao extends GenericDao<Like, Long> {

	public Like likeComment(Comment comment, User user);
	public Like getComment(Comment comment, User user);
	public void unLikeComment(Comment comment, User user);
	
	public Like getPhoto(Photo photo, User user);
	public Like likePhoto(Photo photo, User user);
	public void unLikePhoto(Photo photo, User user);
	
	public Like getTravel(Travel travel, User user);
	public Like likeTravel(Travel travel, User user);
	public void unLikeTravel(Travel travel, User user);
}
