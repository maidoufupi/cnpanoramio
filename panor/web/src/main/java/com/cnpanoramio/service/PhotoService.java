package com.cnpanoramio.service;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoProperties;

public interface PhotoService {

	/**
	 * 转换
	 * 
	 * @param photo
	 * @return
	 */
	public PhotoProperties transform(Photo photo);
	
	/**
	 * 获取图片常规属性
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public PhotoProperties getPhotoProperties(Long id, User user);
	
	/**
	 * 获取图片oss key
	 * 
	 * @param photo
	 * @return
	 */
	public String getPhotoOssKey(Photo photo);
	
}
