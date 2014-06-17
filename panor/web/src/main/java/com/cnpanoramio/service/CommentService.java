package com.cnpanoramio.service;

import java.util.Collection;

import org.appfuse.model.User;

import com.cnpanoramio.json.CommentResponse.Comment;
import com.cnpanoramio.json.PhotoComments;

public interface CommentService {
	
	/**
	 * 创建评论
	 * 
	 * @param comment
	 * @return
	 */
	public Comment save(Comment comment);
	
	/**
	 * 删除评论
	 * 
	 * @param id // comment id
	 * @return
	 */
	public boolean delete(Long id);
	
	/**
	 * 分页获取图片的评论
	 * 
	 * @param id // photo id
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public Collection<Comment> getComments(Long id, int pageSize, int pageNo, User user);
	
	/**
	 * 获取图片评论总数
	 * 
	 * @param id // photo id
	 * @return
	 */
	public Long getCount(Long id);
	
	/**
	 * 修改评论内容
	 * 
	 * @param id
	 * @param content
	 * @return
	 */
	public Comment modify(Long id, String content);
	
}
