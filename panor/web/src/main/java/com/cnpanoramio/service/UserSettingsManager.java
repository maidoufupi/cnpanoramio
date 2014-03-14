package com.cnpanoramio.service;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;

public interface UserSettingsManager {
	
	public UserSettings getCurrentUserSettings();
	public UserSettings getSettingsByUserName(String userName);
	
	/**
	 * 获取用户的公开信息
	 * 
	 * @param id
	 * @return
	 */
	public UserOpenInfo getOpenInfo(Long id);
	
}
