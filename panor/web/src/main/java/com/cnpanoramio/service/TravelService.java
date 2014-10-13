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
	
	public Travel getNoTravel();	
	
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
	 * 往旅行中批量添加图片
	 * 
	 * @param id
	 * @param photos
	 * @return
	 */
	public Travel addTravelPhotos(Long id, List<Long> photos);
	
	/**
	 * 删除旅行的图片
	 * 
	 * @param id
	 * @param photos
	 * @return
	 */
	public Travel removeTravelPhotos(Long id, List<Long> photos);
	
	/**
	 * 修改旅行属性
	 * 
	 * @param travel
	 * @return
	 */
	public Travel changeTravel(Travel travel);
	
	/**
	 * 更改travel描述
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
	 * 更改TravelSpot（景点）的属性
	 * 
	 * @param id
	 * @param spot
	 * @return
	 */
	public TravelSpot changeSpot(Long id, TravelSpot spot);
	
	/**
	 * 更改旅行景点中的图片位置
	 * 
	 * @param id
	 * @param spot
	 */
	public void changeSpotPhotoPostion(Long id, TravelSpot spot);
	
	/**
	 * 获取TravelSpot（景点）的属性
	 * 
	 * @param id
	 * @return
	 */
	public TravelSpot getSpot(Long id);
	
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
