package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.json.TravelResponse.Travel;
import com.cnpanoramio.json.TravelResponse.TravelSpot;

public interface TravelService {
	
	/**
	 * 获取用户的某个travel
	 * 
	 * @param userId
	 * @param travelId
	 * @return
	 */
	public Travel getTravel(Long travelId);

	/**
	 * 获取用户所有travel
	 * 
	 * @param user
	 * @return
	 */
	public List<Travel> getTravels(User user);
	
	/**
	 * 创建用户的旅游
	 * 
	 * @param user
	 * @param travel
	 * @return
	 */
	public List<Travel> createMyTravel(User user, String travel);
	
	/**
	 * 更改travel描述
	 * 
	 * @param id
	 * @param description
	 * @return
	 */
	public Travel changeTravelDesc(Long id, String description);
	
	/**
	 * 更改TravelSpot（景点）的属性
	 * 
	 * @param id
	 * @param spot
	 * @return
	 */
	public TravelSpot changeSpot(Long id, TravelSpot spot);
	
	/**
	 * 获取TravelSpot（景点）的属性
	 * 
	 * @param id
	 * @return
	 */
	public TravelSpot getSpot(Long id);
}
