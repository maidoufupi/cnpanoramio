package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Comment;

public interface CommentDao extends GenericDao<Comment, Long> {

	public Long getCommentSize(Long photoId);
	
	public List<Comment> getComments(Long photoId);
	
	public List<Comment> getCommentPager(Long photoId, Integer pageNo, Integer pageSize);
	
}
