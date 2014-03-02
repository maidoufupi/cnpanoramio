package com.cnpanoramio.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cnpanoramio.json.Comment;
import com.cnpanoramio.json.PhotoComments;


@Path("/comment")
public interface CommentService {
	
	/**
	 * 创建评论
	 * 
	 * @param comment
	 * @return
	 */
	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
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
	public PhotoComments getComments(Long id, int pageSize, int pageNo);
	
	/**
	 * 获取图片评论总数
	 * 
	 * @param id // photo id
	 * @return
	 */
	public Long getCount(Long id);
}
