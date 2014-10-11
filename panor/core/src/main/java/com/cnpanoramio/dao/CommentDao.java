package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;

public interface CommentDao extends GenericDao<Comment, Long> {

	public Comment persist(Comment comment);
	
	public Long getCommentSize(Long photoId);
	
	/**
	 * 图片的全部评论
	 * 
	 * @param photo
	 * @return
	 */
	public List<Comment> getComments(Photo photo);
	
	/**
	 * 分页获取图片的评论
	 * 
	 * @param photo
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Comment> getComments(Photo photo, int pageSize, int pageNo);
	
}
