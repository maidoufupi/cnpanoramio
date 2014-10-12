package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.TravelSpot;

public interface TravelManager extends GenericManager<Travel, Long> {
	
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
	public Travel createMyTravel(User user, String travel);
	
	/**
	 * 向travel中添加photo
	 * 
	 * @param travel
	 * @param photo
	 * @return
	 */
	public Photo addTravelPhoto(Travel travel, Photo photo);
	
	/**
	 * 从旅行中删除图片
	 * 
	 * @param id
	 * @param photos
	 * @return
	 */
	public Travel removeTravelPhotos(Long id, List<Long> photos);
	
	/**
	 * 往旅行中批量添加图片
	 * 
	 * @param id
	 * @param photos
	 * @return
	 */
	public Travel addTravelPhotos(Long id, List<Long> photos);
	
	/**
	 * 更改travel的描述
	 * 
	 * @param id
	 * @param description
	 * @return
	 */
	public Travel changeTravelDesc(Long id, String description);
	
	/**
	 * 更改travel name
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public Travel changeTravelName(Long id, String name);
	
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
	
	/**
	 * 创建景点
	 * 
	 * @param travelSpot
	 * @return
	 */
	public TravelSpot createTravelSpot(Long id, TravelSpot travelSpot);
	
	/**
	 * 往景点中批量添加图片
	 * 
	 * @param id
	 * @param photos
	 * @return
	 */
	public Travel addSpotPhotos(Long id, List<Long> photos);
	
	/**
	 * 往景点中添加图片
	 * 
	 * @param id
	 * @param photo
	 * @return
	 */
	public void addSpotPhoto(TravelSpot spot, Photo photo);
	
	/**
	 * 删除景点
	 * 
	 * @param id
	 * @return
	 */
	public Travel deleteSpot(Long id);
	
	/**
	 * 删除旅行
	 * 
	 * @param id
	 */
	public void deleteTravel(Long id);
	
	/**
	 * 实际删除旅行
	 * 
	 * @param id
	 */
	public void removeTravel(Long id);
	
	/**
	 * 撤销删除
	 * 
	 * @param id
	 */
	public void cancelDeleteTravel(Long id);
}
