package com.cnpanoramio.service;

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
	 * 获取图片oss key
	 * 
	 * @param photo
	 * @return
	 */
	public String getPhotoOssKey(Photo photo);
	
}
