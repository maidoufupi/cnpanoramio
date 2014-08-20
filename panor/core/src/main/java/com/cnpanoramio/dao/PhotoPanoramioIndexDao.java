package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoLatestIndex;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
import com.cnpanoramio.domain.Point;

public interface PhotoPanoramioIndexDao extends
		GenericDao<PhotoPanoramioIndex, PhotoPanoramioIndexPK> {

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
	public List<Photo> getPhotoPanoramio(Point sw, Point ne,
			int level, MapVendor vendor, int width, int height);

	/**
	 * 更新地图图片索引表
	 * 
	 * @return
	 */
	public boolean updatePhotoPanoramioIndex();
	
	/**
	 * 
	 * @return
	 */
	public boolean clearPhotoIndex();
	
	/**
	 * 
	 * @param level
	 * @param west
	 * @param south
	 * @param measure
	 * @return
	 */
	public List<PhotoPanoramioIndex> getPhotoPanoramioIndexList(int level, Double west, Double south, Double measure);
	
	/**
	 * 
	 * @param level
	 * @param west
	 * @param south
	 * @param measure
	 * @return
	 */
	public List<Photo> getPhotoList(int level, Double west, Double south, Double measure);

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
	public List<Photo> getUserPhotoPanoramio(Point sw, Point ne, int level,
			MapVendor vendor, int width, int height, Long userId, boolean favorite);

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
//	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng,
//			Double neLat, Double neLng, int level, MapVendor vendor, int width,
//			int height, Long userId);

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
	public List<Photo> getLatestPanoramio(Point sw, Point ne, int level, MapVendor vendor, int width,
			int height);
	
	/**
	 * 在图片索引中搜索关键字
	 * 
	 * @param sw
	 * @param ne
	 * @param level
	 * @param width
	 * @param height
	 * @param term
	 * @param type
	 * @return
	 */
	public List<Photo> search(Point sw, Point ne, int level, int width, int height, String term, String type);
}
