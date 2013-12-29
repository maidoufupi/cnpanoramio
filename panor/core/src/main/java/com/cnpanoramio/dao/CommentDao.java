package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Comment;

public interface CommentDao extends GenericDao<Comment, Long> {

	public List<Comment> getComments(Long photoId);
	
}
