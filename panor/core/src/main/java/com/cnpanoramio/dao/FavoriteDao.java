package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

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
}
