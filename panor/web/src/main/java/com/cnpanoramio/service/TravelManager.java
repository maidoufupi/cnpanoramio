package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.TravelSpot;

public interface TravelManager {
	
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
	 * 向travel中添加photo
	 * 
	 * @param travel
	 * @param photo
	 * @return
	 */
	public Photo addTravelPhoto(Long id, Photo photo);
	
	/**
	 * 更改travel的描述
	 * 
	 * @param id
	 * @param description
	 * @return
	 */
	public Travel changeTravelDesc(Long id, String description);
	
	/**
	 * 
	 * @param id
	 * @param spot
	 * @return
	 */
	public TravelSpot changeSpot(Long id, TravelSpot spot);
	
	/**
	 * 获取TravelSpot（旅游景点）
	 * @param id
	 * @return
	 */
	public TravelSpot getTravelSpot(Long id);
}
