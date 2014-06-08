package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Photo;

public interface PhotoDao extends GenericDao<Photo, Long>{

	/**
	 * 获取用户全部图片, 按最新到最旧排列图片
	 * 
	 * @param user
	 * @return
	 */
	public List<Photo> getUserPhotos(User user);
	
	/**
	 * 分页获取用户图片, 按最新到最旧排列图片
	 * 
	 * @param user
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Photo> getUserPhotos(User user, int pageSize, int pageNo);
	
	/**
	 * 获取用户拥有照片总数
	 * 
	 * @param user
	 * @return
	 */
	public int getPhotoCount(User user);
	
	/**
	 * 获取用户某个tag的图片总数
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Long getUserPhotoCountBytag(User user, String tag);
	
	/**
	 * 获取用户某个tag的所有图片
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public List<Photo> getUserPhotosByTag(User user, String tag);
	
	/**
	 * 获取用户某个tag的图片(分页)
	 * 
	 * @param user
	 * @param tag
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public List<Photo> getUserPhotoPageByTag(User user, String tag, int pageSize, int pageNo);
			
	/**
	 * 删除照片
	 * 
	 * @param id
	 * @return
	 */
	public Photo delete(Long id);
	
}
