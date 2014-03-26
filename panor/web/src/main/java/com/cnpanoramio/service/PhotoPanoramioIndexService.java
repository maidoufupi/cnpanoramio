package com.cnpanoramio.service;

import java.util.List;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.Point;

public interface PhotoPanoramioIndexService {

	/**
	 * 更新地图上图片信息的索引表
	 * 
	 * @return
	 */
	public boolean updatePanoramioIndex();
	/**
	 * 获取地图上的图片信息
	 * 
	 * @param sw
	 * @param ne
	 * @param level
	 * @param vendor
	 * @param width
	 * @param height
	 * @return
	 */
	public List<PhotoPanoramio> getPanoramio(Point sw, Point ne, int level, MapVendor vendor, int width, int height);
	
	/**
	 * 获取地图上用户自己的图片信息
	 * 
	 * @param swLat
	 * @param swLng
	 * @param neLat
	 * @param neLng
	 * @param level
	 * @param vendor
	 * @param width
	 * @param height
	 * @param userId
	 * @return
	 */
	public List<PhotoPanoramio> getUserPhotoPanoramio(Double swLat, Double swLng, Double neLat, Double neLng, int level, MapVendor vendor, int width, int height, Long userId);
	
	/**
	 * 获取地图上用户收藏的图片信息
	 * 
	 * @param swLat
	 * @param swLng
	 * @param neLat
	 * @param neLng
	 * @param level
	 * @param vendor
	 * @param width
	 * @param height
	 * @param userId
	 * @return
	 */
	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng, Double neLat, Double neLng, int level, MapVendor vendor, int width, int height, Long userId);
	
	/**
	 * 更新最新图片索引表
	 * 
	 * @return
	 */
	public boolean updatePhotoLatestIndex();
	
	/**
	 * 获取最新的图片
	 * 
	 * @param swLat
	 * @param swLng
	 * @param neLat
	 * @param neLng
	 * @param level
	 * @param vendor
	 * @param width
	 * @param height
	 * @param userId
	 * @return
	 */
	public List<PhotoPanoramio> getLatestPanoramio(Double swLat, Double swLng, Double neLat, Double neLng, int level, MapVendor vendor, int width, int height);
}
