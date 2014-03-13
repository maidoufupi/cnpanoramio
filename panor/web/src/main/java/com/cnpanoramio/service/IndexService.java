package com.cnpanoramio.service;

import java.util.Collection;

import com.cnpanoramio.json.PhotoCameraInfo;

public interface IndexService {
	
	/**
	 * 设置主页展示图片
	 * 
	 * @param photoIds
	 * @return
	 */
	public boolean setIndexPhotos(Collection<Long> photoIds);
	
	/**
	 * 获取主页展示图片
	 * 
	 * @return
	 */
	public Collection<PhotoCameraInfo> getIndexPhotos();

}
