package com.cnpanoramio.service;

import java.util.Collection;
import java.util.List;

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
	public Comment save(User user, Comment comment);
	
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
	@Deprecated
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
	
	/**
	 * 转换domain的comment成前端json的comment
	 * 
	 * @param commentD
	 * @return
	 */
	public Comment convertComment(com.cnpanoramio.domain.Comment commentD, User user);
	
	/**
	 * 转换comment列表
	 * 
	 * @param commentDs
	 * @return
	 */
	public List<Comment> convertComments(List<com.cnpanoramio.domain.Comment> commentDs, User user);
	
	/**
	 * 获取图片的评论
	 * 
	 * @param photoId
	 * @return
	 */
	public List<Comment> getPhotoComments(Long photoId, User user);
}
