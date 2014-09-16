package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Travel;

public interface TravelDao extends GenericDao<Travel, Long> {
	
	public Travel persist(Travel travel);
	
	/**
	 * 通过名称获取用户travel
	 * 
	 * @param userId
	 * @param name
	 * @return
	 */
	public Travel getByName(Long userId, String name);
	
	public List<Travel> searchTravel(String term);
}
