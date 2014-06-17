package com.cnpanoramio.service;

import com.cnpanoramio.domain.Like;

public interface LikeManager {

	/**
	 * 赞或取消 图片
	 * 
	 * @param id
	 * @return
	 */
	public Like likePhoto(Long id);
	
	/**
	 * 赞或取消 评论
	 * 
	 * @param id
	 * @return
	 */
	public Like likeComment(Long id);
	
	/**
	 * 赞或取消 旅行
	 * 
	 * @param id
	 * @return
	 */
	public Like likeTravel(Long id);
}
