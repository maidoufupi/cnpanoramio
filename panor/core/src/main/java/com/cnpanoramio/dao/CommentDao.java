package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Message;

public interface CommentDao extends GenericDao<Comment, Long> {

	public Comment persist(Comment comment);
	
	public Long getCommentSize(Long photoId);
	
	public List<Comment> getComments(Long photoId);
	
//	public List<Comment> getCommentPager(Long photoId, int pageSize, int pageNo);
	
}
