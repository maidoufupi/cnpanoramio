package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;

public interface PhotoPanoramioIndexDao extends GenericDao<PhotoPanoramioIndex, PhotoPanoramioIndex.PhotoPanoramioIndexPK> {
	
	/**
	 * 获取地图图片
	 * 
	 * @param sw
	 * @param ne
	 * @param level
	 * @param vendor
	 * @param width
	 * @param height
	 * @return
	 */
	public List<PhotoPanoramio> getPhotoPanoramio(Point sw, Point ne, int level, MapVendor vendor, int width, int height);
	
	/**
	 * 更新地图图片索引表
	 * 
	 * @return
	 */
	public boolean updatePhotoPanoramioIndex();
	
	/**
	 * 获取用户自己的图片
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
	 * 获取用户收藏的图片
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
}
