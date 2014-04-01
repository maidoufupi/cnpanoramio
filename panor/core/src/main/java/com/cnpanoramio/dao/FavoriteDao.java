package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Favorite;

public interface FavoriteDao extends GenericDao<Favorite, Favorite.PK> {

	/**
	 * 获取Favorite
	 * 
	 * @param photoId
	 * @param userId
	 * @return
	 */
	public Favorite get(Long photoId, Long userId);
	
	/**
	 * 获取用户图片被收藏总数
	 * 
	 * @param user
	 * @return
	 */
	public Long getUserPhotoFavoriteCount(User user);
}
