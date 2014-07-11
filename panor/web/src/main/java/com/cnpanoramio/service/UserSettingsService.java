package com.cnpanoramio.service;

import java.util.List;

import com.cnpanoramio.json.UserResponse.Recycle;


public interface UserSettingsService {
	
	/**
	 * 获取用户回收站内容
	 * 
	 * @param id
	 * @return
	 */
	public List<Recycle> getRecycleBin(Long id);
}
