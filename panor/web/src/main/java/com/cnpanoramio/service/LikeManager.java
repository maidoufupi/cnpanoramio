package com.cnpanoramio.service;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;

public interface LikeManager {

	/**
	 * 赞或取消 图片
	 * 
	 * @param id
	 * @return
	 */
	public Like likePhoto(User user, Long id);
	
	public void unLikePhoto(User user, Long id);
	
	/**
	 * 赞或取消 评论
	 * 
	 * @param id
	 * @return
	 */
	public Like likeComment(User user, Long id);
	public void unLikeComment(User user, Long id);
	
	/**
	 * 赞或取消 旅行
	 * 
	 * @param id
	 * @return
	 */
	public Like likeTravel(User user, Long id);
	public void unLikeTravel(User user, Long id);
	
	public Like getLikePhoto(User user, Photo photo);
	public Like getLikeComment(User user, Comment comment);
	public Like getLikeTravel(User user, Travel travel);
}
